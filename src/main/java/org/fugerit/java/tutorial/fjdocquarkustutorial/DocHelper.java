package org.fugerit.java.tutorial.fjdocquarkustutorial;

import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

/**
 * DocHelper, version : auto generated on 2024-09-25 23:53:33.687
 */
public class DocHelper {

     private FreemarkerDocProcessConfig docProcessConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fj-doc-quarkus-tutorial/fm-doc-process-config.xml" );

     public FreemarkerDocProcessConfig getDocProcessConfig() { return this.docProcessConfig; }

}
