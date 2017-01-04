package com.restapi;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import javax.ws.rs.Produces;
import java.io.File;
import java.io.StringWriter;
import java.sql.Timestamp;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Resource which has only one representation.
 *
 */
public class MockRestRessource extends ServerResource {

    @Get("xml") @Produces("application/xml")
    public String getResource() {
        String uid = (String)this.getRequestAttributes().get("uid");

        String xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xmlResponse = xmlResponse + "<guestbook>";
        xmlResponse = xmlResponse + "<greeting>";
        xmlResponse = xmlResponse + "<author_email>author_email</author_email>";
        xmlResponse = xmlResponse + "<author_id>author_id</author_id>";
        xmlResponse = xmlResponse + "<content>content</content>";
        xmlResponse = xmlResponse + "</greeting>";
        xmlResponse = xmlResponse + "</guestbook>";


        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("userRequest");
            doc.appendChild(rootElement);

            // sub elements
            Element qrElement = doc.createElement("qr");
            qrElement.appendChild(doc.createTextNode("http://ase-pi-project.appspot.com/rest/mockService/" + uid));
            rootElement.appendChild(qrElement);

            Element idElement = doc.createElement("id");
            idElement.appendChild(doc.createTextNode(uid));
            rootElement.appendChild(idElement);

            Element timestampElement = doc.createElement("timestamp");
            timestampElement.appendChild(doc.createTextNode(new Timestamp(System.currentTimeMillis()).toString()));
            rootElement.appendChild(timestampElement);

            // write the content into xml file
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            // Output to console for testing
            StreamResult consoleResult =
                    new StreamResult(System.out);
            transformer.transform(source, consoleResult);

            return outputToString(doc);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe){
            tfe.printStackTrace();
        }
        return xmlResponse;
    }

    public String outputToString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

}