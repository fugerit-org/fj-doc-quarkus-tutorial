package org.fugerit.java.demo.fjdocquarkustutorial;

import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DocHelper {

    private FreemarkerDocProcessConfig docProcessConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fj-doc-quarkus-tutorial/fm-doc-process-config.xml" );

    public FreemarkerDocProcessConfig getDocProcessConfig() { return this.docProcessConfig; }

}
