package it.polimi.rsp.wasp.cqels;

import it.polimi.sr.wasp.rsp.model.InternalTaskWrapper;
import it.polimi.sr.wasp.server.model.concept.Channel;
import lombok.extern.java.Log;
import org.deri.cqels.engine.ContinuousConstruct;
import org.deri.cqels.engine.ContinuousSelect;
import org.deri.cqels.engine.OpRouter1;

import java.util.List;

@Log
public class CQELSQuery extends InternalTaskWrapper {

    private final OpRouter1 int_query;

    public CQELSQuery(String qid, String base, String query, ContinuousConstruct int_query, CQELSQueryResultStream out, List<Channel> ins) {
        super(qid, query, base);
        this.out = out;
        this.int_query = int_query;
        this.in.addAll(ins);
    }

    public CQELSQuery(String qid, String base, String query, ContinuousSelect int_query, CQELSQueryResultStream out, List<Channel> ins) {
        super(qid, query, base);
        this.int_query = int_query;
        this.out = out;
        this.in.addAll(ins);

    }

}
