package it.polimi.rsp.wasp.cqels;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.graph.GraphFactory;
import it.polimi.sr.wasp.rsp.model.StatelessDataChannel;
import lombok.extern.java.Log;
import org.deri.cqels.data.Mapping;
import org.deri.cqels.engine.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log
public class CQELSQueryResultStream extends StatelessDataChannel {
    private final OpRouter1 internal_query;
    private final ExecContext context;


    public CQELSQueryResultStream(String id, String base, String qid, ExecContext context, ContinuousConstruct int_query) {
        super(id, base, qid);
        this.context = context;
        this.internal_query = int_query;
        int_query.register(new CQELSConstructListener(context));
    }

    public CQELSQueryResultStream(String id, String base, String qid, ExecContext context, ContinuousSelect int_query) {
        super(id, base, qid);
        this.context = context;
        this.internal_query = int_query;
        int_query.register(new CQELSSelectListener(context));

    }

    private class CQELSConstructListener extends ConstructListener {

        public CQELSConstructListener(ExecContext context) {
            super(context);
        }

        @Override
        public void update(List<Triple> triples) {
            put(serializeTriples(triples));
        }
    }

    private class CQELSSelectListener implements ContinuousListener {

        private ExecContext context = null;

        public CQELSSelectListener(ExecContext context) {
            this.context = context;
        }

        public void update(Mapping mapping) {
            Iterator<Var> vars = mapping.vars();
            List<Node> nodes = new ArrayList<>();
            while (vars.hasNext()) {
                Node n = context.engine().decode(mapping.get(vars.next()));
                nodes.add(n);
            }
            put(serializeNodes(nodes));
        }
    }

    public static String serializeTriples(List<Triple> triples) {

        Graph g = GraphFactory.createDefaultGraph();
        for (Triple t : checkTriplesForLiterals(triples)) {
            g.add(t);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Model m = ModelFactory.createModelForGraph(g);
        m.write(os, "JSON-LD");

        return os.toString();
    }

    private static List<Triple> checkTriplesForLiterals(List<Triple> triples) {
        List<Triple> checkedTriples = new ArrayList<Triple>();

        for (Triple triple : triples) {
            String[] object = triple.getObject().toString().split("\\^\\^");
            if (object.length == 2) {
                String value = object[0].replace("\"", "");
                RDFDatatype dataType = TypeMapper.getInstance().getTypeByName(object[1]);
                checkedTriples.add(new Triple(triple.getSubject(), triple.getPredicate(), NodeFactory.createLiteral(value, dataType)));
            } else {
                checkedTriples.add(triple);
            }
        }

        return checkedTriples;
    }


    public static String serializeNodes(List<Node> nodes) {
        StringBuilder sBuilder = new StringBuilder();
        for (Node n : nodes) {
            sBuilder.append(n.toString()).append(" ");
        }

        return sBuilder.toString();
    }
}
