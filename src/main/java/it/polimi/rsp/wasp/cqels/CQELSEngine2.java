package it.polimi.rsp.wasp.cqels;

import it.polimi.sr.wasp.rsp.RSPEngine;
import it.polimi.sr.wasp.rsp.model.Query;
import it.polimi.sr.wasp.server.model.concept.Channel;
import lombok.extern.java.Log;
import org.deri.cqels.engine.*;

@Log
public class CQELSEngine2 extends RSPEngine {

    private ExecContext context = null;
    CQELSEngine engine;

    public CQELSEngine2(String name, String base, String db) {
        super(name, base);
        this.context = new ExecContext(db, false);
        this.engine = new CQELSEngine(context);
    }

    @Override
    protected Query handleInternalQuery(String qid, String query, String uri, String source) {

        if (query.contains("SELECT")) {
            ContinuousSelect int_query = context.registerSelect(query);
            CQELSQueryResultStream out = new CQELSQueryResultStream(uri, qid, context, int_query);
            CQELSQuery q = new CQELSQuery(qid, query, int_query, out);
            out.apply(q);
            return q;
        } else if (query.contains("CONSTRUCT")) {
            ContinuousConstruct int_query = context.registerConstruct(query);
            CQELSQueryResultStream out = new CQELSQueryResultStream(uri, qid, context, int_query);
            CQELSQuery q = new CQELSQuery(qid, query, int_query, out);
            out.apply(q);

            return q;
        }


        return null;
    }

    @Override
    protected Channel handleInternalStream(String id, String source) {
        return new CQELSRDFStream(id, source, new CQELSInternalSink(new TripleStream(context, id)));
    }

    private class TripleStream extends RDFStream {
        public TripleStream(ExecContext context, String id) {
            super(context, id);
        }

        @Override
        public void stop() {
            throw new UnsupportedOperationException();
        }
    }
}