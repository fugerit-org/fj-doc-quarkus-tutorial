package org.fugerit.java.demo.fjdocquarkustutorial;

import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

/**
 * DocHelper, version : auto generated on 2024-10-16 22:57:47.201
 */
public class DocHelper {

     private FreemarkerDocProcessConfig docProcessConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fj-doc-quarkus-tutorial/fm-doc-process-config.xml" );

     public FreemarkerDocProcessConfig getDocProcessConfig() { return this.docProcessConfig; }

}
