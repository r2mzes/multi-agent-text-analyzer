package pl.edu.agh.text.analyzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.*;

public class Classifier {
    public String classify(String text) throws IOException, ParserConfigurationException, SAXException {
        String response = this.makeRequest(text);
        String label = this.getLabel(response);
        return label;
    }

    public String makeRequest(String text) throws IOException {
        final String api = "http://api.meaningcloud.com/class-1.1";
        final String key = "731de1f60bad5d55bbfc1bae95705f36";
        final String model = "IPTC_en";

        Request request = new Request(api);
        request.addParameter("key", key);
        request.addParameter("txt", text);
        request.addParameter("model", model);
        request.addParameter("of", "xml");
        return request.getResponse();
    }

    public String getLabel(String response) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
        Document document = builder.parse(stream);
        document.getDocumentElement().normalize();
        Element element = document.getDocumentElement();

        NodeList statusList = element.getElementsByTagName("status");
        Node status = statusList.item(0);
        NamedNodeMap attributes = status.getAttributes();
        Node code = attributes.item(0);

        if (!code.getTextContent().equals("0")) {
            return "";
        }

        NodeList categoryList = element.getElementsByTagName("category_list");

        if (categoryList.getLength() <= 0) {
            return "";
        }

        Node categories = categoryList.item(0);
        NodeList category = categories.getChildNodes();

        Node infoCategory = category.item(0);
        NodeList children = infoCategory.getChildNodes();
        String label = "";

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);

            if (node.getNodeName().equals("label")) {
                label = node.getTextContent();
                break;
            }
        }

        String[] parts = label.split(" - ");
        return parts[0];
    }
}
