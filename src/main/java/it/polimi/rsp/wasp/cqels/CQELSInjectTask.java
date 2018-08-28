package it.polimi.rsp.wasp.cqels;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import it.polimi.sr.wasp.server.model.concept.Channel;
import it.polimi.sr.wasp.server.model.concept.tasks.SynchTask;
import lombok.AllArgsConstructor;
import org.apache.jena.riot.Lang;
import org.deri.cqels.engine.RDFStream;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@AllArgsConstructor
public class CQELSInjectTask implements SynchTask {
    private final RDFStream stream;

    private static Stream<Triple> deserialize(String s) {
        Model m = ModelFactory.createDefaultModel()
                .read(new ByteArrayInputStream(s.getBytes()), null, Lang.JSONLD.getLabel());

        StmtIterator i = m.listStatements();
        List<Triple> triples = new ArrayList<>();
        while (i.hasNext())
            triples.add(i.nextStatement().asTriple());
        return triples.stream();
    }

    @Override
    public String iri() {
        return stream.getURI();
    }

    @Override
    public Channel out() {
        return null;
    }

    @Override
    public Channel[] in() {
        return new Channel[0];
    }

    @Override
    public void await(String s) {
        deserialize(s).forEach(stream::stream);

    }
}
