package it.polimi.rsp.wasp.cqels;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import it.polimi.sr.wasp.server.model.concept.Channel;
import it.polimi.sr.wasp.server.model.concept.Sink;
import it.polimi.sr.wasp.server.model.concept.Source;
import lombok.AllArgsConstructor;
import org.apache.jena.riot.Lang;
import org.deri.cqels.engine.RDFStream;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@AllArgsConstructor
public class CQELSInternalSink implements Sink {
    private final RDFStream stream;

    @Override
    public void await(Source source, String s) {
        deserialize(s).forEach(stream::stream);
    }

    @Override
    public void await(Channel channel, String s) {
        deserialize(s).forEach(stream::stream);
    }

    private static Stream<Triple> deserialize(String s) {
        Model m = ModelFactory.createDefaultModel()
                .read(new ByteArrayInputStream(s.getBytes()), null, Lang.JSONLD.getLabel());

        StmtIterator i = m.listStatements();
        List<Triple> triples = new ArrayList<>();
        while (i.hasNext())
            triples.add(i.nextStatement().asTriple());
        return triples.stream();
    }
}
