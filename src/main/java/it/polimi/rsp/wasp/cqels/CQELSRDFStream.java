package it.polimi.rsp.wasp.cqels;

import it.polimi.sr.wasp.rsp.model.StatelessDataChannel;

public class CQELSRDFStream extends StatelessDataChannel {
    public CQELSRDFStream(String id, String source, String base, CQELSInjectTask task) {
        super(id, source, base);
        this.asynch_task.add(task);
    }
}
