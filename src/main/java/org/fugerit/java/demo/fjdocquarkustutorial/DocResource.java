package org.fugerit.java.demo.fjdocquarkustutorial;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.ObjectUtils;
import org.fugerit.java.core.util.checkpoint.SimpleCheckpoint;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.script.*;

@Slf4j
@Path("/doc")
@Tag( name = "XML SOURCE (DEFAULT)", description = "Add query param sourceType=1 or no value to use the XML source example")
@Tag( name = "JSON SOURCE", description = "Add query param sourceType=2 to use the JSON source example")
@Tag( name = "YAML SOURCE", description = "Add query param sourceType=3 to use the YAML source example")
@Tag( name = "KTS SOURCE", description = "Add query param sourceType=9 to use the Kotlin source example")
public class DocResource {


    private DocHelper docHelper = new DocHelper();

    /*
     * Kotlin support still experimental
     */
    private void handleKts( List<People> listPeople, String handlerId, ByteArrayOutputStream baos ) {
        SafeFunction.apply( () -> {
            SimpleCheckpoint checkpoint = new SimpleCheckpoint();
            ScriptEngineManager manager = new ScriptEngineManager();
            log.info( "kts create script manager : {}", checkpoint.getFormatTimeDiffMillis() );
            ScriptEngine engine =  manager.getEngineByExtension( "kts" );
            log.info( "kts create script engine : {}", checkpoint.getFormatTimeDiffMillis() );
            Bindings bindings = engine.createBindings();
            log.info( "kts create script bindings : {}", checkpoint.getFormatTimeDiffMillis() );
            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            data.put( "docTitle", "My sample title Source KTS" );
            data.put( "listPeople", mapper.readValue( mapper.writeValueAsString( listPeople ) , ArrayList.class ) );
            log.info( "kts read json data : {}", checkpoint.getFormatTimeDiffMillis() );
            bindings.put( "data", data );
            engine.setBindings( bindings, ScriptContext.ENGINE_SCOPE );
            try (InputStream is = ClassHelper.loadFromDefaultClassLoader( "fj-doc-quarkus-tutorial/kts/document.kts" )) {
                log.info( "kts set bindings : {}", checkpoint.getFormatTimeDiffMillis() );
                Object obj = engine.eval( StreamIO.readString( is ) );
                log.info( "kts eval script : {}", checkpoint.getFormatTimeDiffMillis() );
                String xml = obj.toString();
                log.info( "kts toXml : {}", checkpoint.getFormatTimeDiffMillis() );
                try ( StringReader xmlReader = new StringReader( xml) ) {
                    DocTypeHandler handler = this.docHelper.getDocProcessConfig().getFacade().findHandler( handlerId );
                    handler.handle( DocInput.newInput( handler.getType(), xmlReader ), DocOutput.newOutput( baos ) );
                }
            }
        } );
    }

    byte[] processDocument(String handlerId, Integer sourceType) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int realSourceType = ObjectUtils.objectWithDefault( sourceType, DocFacadeSource.SOURCE_TYPE_DEFAULT );
            log.info( "using source type : {}", realSourceType );
            // create custom data for the fremarker template 'document.ftl'
            List<People> listPeople = Arrays.asList(new People("Luthien", "Tinuviel", "Queen"), new People("Thorin", "Oakshield", "King"));
            // output generation
            DocProcessContext context = DocProcessContext.newContext("listPeople", listPeople);
            switch ( realSourceType ) {
                case DocFacadeSource.SOURCE_TYPE_JSON -> docHelper.getDocProcessConfig().fullProcess("document-json", context.withSourceType( realSourceType ), handlerId, baos);
                case DocFacadeSource.SOURCE_TYPE_YAML -> docHelper.getDocProcessConfig().fullProcess("document-yaml", context.withSourceType( realSourceType ), handlerId, baos);
                case 9 -> handleKts( listPeople, handlerId, baos );
                default -> docHelper.getDocProcessConfig().fullProcess("document", context, handlerId, baos);
            }
            // return the output
            return baos.toByteArray();
        } catch (Exception e) {
            String message = String.format("Error processing %s, error:%s", handlerId, e);
            log.error(message, e);
            throw new WebApplicationException(message, e);
        }
    }

    @APIResponse(responseCode = "200", description = "The Markdown document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "markdown" )
    @Operation( operationId = "MarkdownExample", summary = "Example Markdown generation",
            description =  "Generates an example Markdown document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/markdown")
    @Path("/example.md")
    public byte[] markdownExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument(DocConfig.TYPE_MD, sourceType);
    }

    @APIResponse(responseCode = "200", description = "The HTML document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "html" )
    @Operation( operationId = "HTMLExample", summary = "Example HTML generation",
            description =  "Generates an example HTML document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/html")
    @Path("/example.html")
    public byte[] htmlExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument(DocConfig.TYPE_HTML, sourceType);
    }

    @APIResponse(responseCode = "200", description = "The AsciiDoc document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "asciidoc" )
    @Operation( operationId = "AsciiDocExample", summary = "Example AsciiDoc generation",
            description =  "Generates an example AsciiDoc document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/asciidoc")
    @Path("/example.adoc")
    public byte[] asciidocExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument(DocConfig.TYPE_ADOC, sourceType);
    }

    @APIResponse(responseCode = "200", description = "The PDF document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "pdf" )
    @Operation( operationId = "PDFExample", summary = "Example PDF generation",
            description =  "Generates an example PDF document using Fugerit Venus Doc handler" )
    @GET
    @Produces("application/pdf")
    @Path("/example.pdf")
    public byte[] pdfExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument(DocConfig.TYPE_PDF, sourceType);
    }

    @APIResponse(responseCode = "200", description = "The Excel document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "excel" )
    @Operation( operationId = "ExcelExample", summary = "Example Excel generation",
            description =  "Generates an example Excel document using Fugerit Venus Doc handler" )
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Path("/example.xlsx")
    public byte[] excelExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument(DocConfig.TYPE_XLSX, sourceType);
    }

    @APIResponse(responseCode = "200", description = "The CSV document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "csv" )
    @Operation( operationId = "CSVExample", summary = "Example CSV generation",
            description =  "Generates an example CSV document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/csv")
    @Path("/example.csv")
    public byte[] csvExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument(DocConfig.TYPE_CSV, sourceType);
    }

    @APIResponse(responseCode = "200", description = "The OpenPDF document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "openpdf" )
    @Operation( operationId = "OpenPDFExample", summary = "Example OpenPDF generation",
            description =  "Generates an example OpenPDF document using Fugerit Venus Doc handler" )
    @GET
    @Produces("application/pdf")
    @Path("/openpdf/example.pdf")
    public byte[] openpdfExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument(DocConfig.TYPE_PDF, sourceType);
    }
    @APIResponse(responseCode = "200", description = "The OpenPDFHTML document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "openpdfhtml" )
    @Operation( operationId = "OpenPDFHTMLExample", summary = "Example OpenPDFHTML generation",
            description =  "Generates an example OpenPDFHTML document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/html")
    @Path("/openpdf/example.html")
    public byte[] openpdfhtmlExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument(DocConfig.TYPE_HTML, sourceType);
    }
    @APIResponse(responseCode = "200", description = "The RTF document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "rtf" )
    @Operation( operationId = "RTFExample", summary = "Example RTF generation",
            description =  "Generates an example RTF document using Fugerit Venus Doc handler" )
    @GET
    @Produces("application/rtf")
    @Path("/example.rtf")
    public byte[] rtfExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument(DocConfig.TYPE_RTF, sourceType);
    }
    @APIResponse(responseCode = "200", description = "The PDF document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tag( name = "document" )
    @Tag( name = "pdf" )
    @Operation( operationId = "PDFA1bExample", summary = "Example PDF/A-1b generation",
            description =  "Generates an example PDF document using Fugerit Venus Doc handler" )
    @GET
    @Produces("application/pdf")
    @Path("/pdfa1b/example.pdf")
    public byte[] pdfA1bExample(@QueryParam("sourceType") Integer sourceType) {
        return processDocument("PDF/A-1b", sourceType);
    }

}
