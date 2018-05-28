package it.polimi.rsp.wasp.cqels;

import it.polimi.deib.rsp.vocals.rdf4j.VocalsFactoryRDF4J;
import it.polimi.sr.wasp.rsp.RSPServer;
import it.polimi.sr.wasp.server.model.persist.StatusManager;
import lombok.extern.java.Log;
import spark.Spark;

import java.io.IOException;

@Log
public class CQELSServer extends RSPServer {

    public CQELSServer() throws IOException {
        super(new VocalsFactoryRDF4J());
    }

    public static void main(String[] args) throws IOException {
        CQELSEngine2 cqels = new CQELSEngine2("cqels", "http://localhost:8181", args[1]);
        if (args.length > 0) {
            new CQELSServer().start(cqels, args[0]);
            log.info("Running at http://localhost:8181/cqels");
        } else {
            new CQELSServer().start(cqels, CQELSEngine2.class.getResource("default.properties").getPath());
            log.info("Running at http://localhost:8181/cqels");
        }
    }

    @Override
    protected void ingnite(String host, String name, int port) {
        super.ingnite(host, name, port);
        Spark.get(name + "/observers", (request, response) -> StatusManager.sinks.values());
    }
}
