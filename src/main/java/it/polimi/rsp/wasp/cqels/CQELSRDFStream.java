package it.polimi.rsp.wasp.cqels;

import it.polimi.sr.wasp.rsp.model.Stream;

public class CQELSRDFStream extends Stream {
    public CQELSRDFStream(String id, String source, CQELSInternalSink cqelsInternalSink) {
        super(id, source);
        this.sinks.add(cqelsInternalSink);
    }
}
