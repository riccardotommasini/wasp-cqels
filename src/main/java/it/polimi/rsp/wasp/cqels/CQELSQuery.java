package it.polimi.rsp.wasp.cqels;

import it.polimi.sr.wasp.rsp.model.Query;
import lombok.extern.java.Log;
import org.deri.cqels.engine.ContinuousConstruct;
import org.deri.cqels.engine.ContinuousSelect;
import org.deri.cqels.engine.OpRouter1;

@Log
public class CQELSQuery extends Query {

    private final OpRouter1 int_query;

    public CQELSQuery(String qid, String query, ContinuousConstruct int_query, CQELSQueryResultStream out) {
        super(qid, query);
        this.out = out;
        this.int_query = int_query;
    }

    public CQELSQuery(String qid, String query, ContinuousSelect int_query, CQELSQueryResultStream out) {
        super(qid, query);
        this.int_query = int_query;
    }

}
