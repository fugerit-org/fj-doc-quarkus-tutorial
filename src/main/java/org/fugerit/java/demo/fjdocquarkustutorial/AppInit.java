package org.fugerit.java.demo.fjdocquarkustutorial;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.inject.Inject;
import org.apache.fop.apps.*;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.config.InitHandler;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;

@Slf4j
@ApplicationScoped
@RegisterForReflection( targets = { DocHelper.class, People.class, FreemarkerDocProcessConfig.class } )
public class AppInit {

    @Inject
    DocHelper docHelper;

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
        /*
         * This will initialize all the doc handlers using async mode.
         * (use method InitHandler.initDocAll() for synced startup)
         */
        //InitHandler.initDocAllAsync(
        //docHelper.getDocProcessConfig().getFacade().handlers() );
        SafeFunction.apply( () -> {
            FopFactory fopFactory = FopFactory.newInstance( new File( "./src/main/resources/fj-doc-quarkus-tutorial/fop-config.xml" ));
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                // Step 3: Construct fop with desired output format
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
                // Step 4: Setup JAXP using identity transformer
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(); // identity transformer
                // Step 5: Setup input and output for XSLT transformation
                // Setup input stream
                Source src = new StreamSource(new File("./src/main/resources/test.fo"));
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
                // Step 6: Start XSLT transformation and FOP processing
                transformer.transform(src, res);
                log.info( "fop generation ok : {}", out.toByteArray().length );
            }
        } );
    }

}
