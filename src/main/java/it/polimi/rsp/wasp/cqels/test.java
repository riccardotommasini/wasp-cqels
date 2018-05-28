package it.polimi.rsp.wasp.cqels;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.commons.io.IOUtils;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.*;

import static java.lang.System.out;

public class test {
    public static void main(String[] args) throws IOException {

        String s = IOUtils.toString(new FileInputStream("/Users/riccardo/_Projects/RSP/wasp-cqels/src/main/resources/input.json"));


        Model model = ModelFactory.createDefaultModel().read(new ByteArrayInputStream(s.getBytes()), null, Lang.JSONLD.getLabel());
        model.write(out, "JSON-LD");

    }
}
