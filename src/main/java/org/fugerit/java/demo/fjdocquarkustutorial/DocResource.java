package org.fugerit.java.demo.fjdocquarkustutorial;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

@Slf4j
@Path("/doc")
public class DocResource {

    byte[] processDocument(String handlerId) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // creates the doc helper
            DocHelper docHelper = new DocHelper();
            // create custom data for the fremarker template 'document.ftl'
            List<People> listPeople = Arrays.asList(new People("Luthien", "Tinuviel", "Queen"), new People("Thorin", "Oakshield", "King"));
            // output generation
            docHelper.getDocProcessConfig().fullProcess("document", DocProcessContext.newContext("listPeople", listPeople), handlerId, baos);
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
    @Tags( { @Tag( name = "document" ), @Tag( name = "markdown" ) } )
    @Operation( operationId = "MarkdownExample", summary = "Example Markdown generation",
        description =  "Generates an example Markdown document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/markdown")
    @Path("/example.md")
    public byte[] markdownExample() {
        return processDocument(DocConfig.TYPE_MD);
    }

    @APIResponse(responseCode = "200", description = "The HTML document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "html" ) } )
    @Operation( operationId = "HTMLExample", summary = "Example HTML generation",
        description =  "Generates an example HTML document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/html")
    @Path("/example.html")
    public byte[] htmlExample() {
        return processDocument(DocConfig.TYPE_HTML);
    }

    @APIResponse(responseCode = "200", description = "The AsciiDoc document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "asciidoc" ) } )
    @Operation( operationId = "AsciiDocExample", summary = "Example AsciiDoc generation",
        description =  "Generates an example AsciiDoc document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/asciidoc")
    @Path("/example.adoc")
    public byte[] asciidocExample() {
        return processDocument(DocConfig.TYPE_ADOC);
    }




    @APIResponse(responseCode = "200", description = "The OpenPDF document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "openpdf" ) } )
    @Operation( operationId = "OpenPDFExample", summary = "Example OpenPDF generation",
        description =  "Generates an example OpenPDF document using Fugerit Venus Doc handler" )
    @GET
    @Produces("application/pdf")
    @Path("/openpdf/example.pdf")
    public byte[] openpdfExample() {
        return processDocument(DocConfig.TYPE_PDF);
    }
    @APIResponse(responseCode = "200", description = "The OpenPDFHTML document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "openpdfhtml" ) } )
    @Operation( operationId = "OpenPDFHTMLExample", summary = "Example OpenPDFHTML generation",
        description =  "Generates an example OpenPDFHTML document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/html")
    @Path("/openpdf/example.html")
    public byte[] openpdfhtmlExample() {
        return processDocument(DocConfig.TYPE_HTML);
    }

}
