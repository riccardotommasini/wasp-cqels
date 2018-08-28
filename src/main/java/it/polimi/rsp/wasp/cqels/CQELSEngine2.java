package it.polimi.rsp.wasp.cqels;

import it.polimi.sr.wasp.rsp.RSPEngine;
import it.polimi.sr.wasp.rsp.model.InternalTaskWrapper;
import it.polimi.sr.wasp.rsp.model.QueryBody;
import it.polimi.sr.wasp.server.model.concept.Channel;
import lombok.extern.java.Log;
import org.deri.cqels.engine.*;

import java.util.List;

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
    protected InternalTaskWrapper handleInternalQuery(String qid, String query, String uri, String source, List<Channel> list) {

        if (query.contains("SELECT")) {
            ContinuousSelect int_query = context.registerSelect(query);
            CQELSQueryResultStream out = new CQELSQueryResultStream(uri, base, qid, context, int_query);
            CQELSQuery q = new CQELSQuery(qid, base, query, int_query, out, list);
            out.add(q);
            return q;
        } else if (query.contains("CONSTRUCT")) {
            ContinuousConstruct int_query = context.registerConstruct(query);
            CQELSQueryResultStream out = new CQELSQueryResultStream(uri, base, qid, context, int_query);
            CQELSQuery q = new CQELSQuery(qid, base, query, int_query, out, list);
            out.add(q);
            return q;
        }

        return null;
    }

    @Override
    protected String[] extractStreams(QueryBody queryBody) {
        return new String[0];
    }


    @Override
    protected Channel handleInternalStream(String id, String source) {
        return new CQELSRDFStream(id, base, source, new CQELSInjectTask(new TripleStream(context, id)));
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