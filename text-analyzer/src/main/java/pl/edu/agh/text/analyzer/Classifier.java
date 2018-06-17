package pl.edu.agh.text.analyzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import pl.edu.agh.corpus.provider.model.Tag;

public class Classifier {
	public Tag classify(String text) throws IOException, ParserConfigurationException, SAXException {
		String response = this.makeRequest(text);
		Tag tag = this.getTag(response);
		return tag;
	}

	public String makeRequest(String text) throws IOException {
		final String api = "http://api.meaningcloud.com/class-1.1";
		final String key = "3b1c6b38cb1c7d83603f0da0322a6da6";
		final String model = "IPTC_en";

		Request request = new Request(api);
		request.addParameter("key", key);
		request.addParameter("txt", text);
		request.addParameter("model", model);
		request.addParameter("of", "xml");
		return request.getResponse();
	}

	public Tag getTag(String response) throws IOException, ParserConfigurationException, SAXException {
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
			return new Tag(null, null, null);
		}

		NodeList categoryList = element.getElementsByTagName("category_list");

		if (categoryList.getLength() <= 0) {
			return new Tag(null, null, null);
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
		String firstLevelTag = (parts.length > 0) ? parts[0] : null,
				secondLevelTag = (parts.length > 1) ? parts[1] : null,
				thirdLevelTag = (parts.length > 2) ? parts[2] : null;

		return new Tag(firstLevelTag, secondLevelTag, thirdLevelTag);
	}
}
