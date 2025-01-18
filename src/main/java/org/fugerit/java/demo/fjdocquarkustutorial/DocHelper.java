package org.fugerit.java.demo.fjdocquarkustutorial;

import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;

/**
 * DocHelper, version : auto generated on 2025-01-18 01:58:35.952
 */
public class DocHelper {

     private FreemarkerDocProcessConfig docProcessConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fj-doc-quarkus-tutorial/fm-doc-process-config.xml" );

     public FreemarkerDocProcessConfig getDocProcessConfig() { return this.docProcessConfig; }

}
