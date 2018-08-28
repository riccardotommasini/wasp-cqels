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
        if (args.length > 0) {
            String db = args[1];
            CQELSEngine2 cqels = new CQELSEngine2("cqels", "http://localhost:8181", db);
            new CQELSServer().start(cqels, args[0]);
            log.info("Running at http://localhost:8181/cqels");
        } else {
            CQELSEngine2 cqels = new CQELSEngine2("cqels", "http://localhost:8181", CQELSEngine2.class.getClassLoader().getResource("CQELS_DB").getPath());
            new CQELSServer().start(cqels, CQELSEngine2.class.getClassLoader().getResource("default.properties").getPath());
            log.info("Running at http://localhost:8181/cqels");
        }
    }

    @Override
    protected void ignite(String host, String name, int port) {
        super.ignite(host, name, port);
        Spark.get(name + "/observers", (request, response) -> StatusManager.sinks.values());
    }
}
