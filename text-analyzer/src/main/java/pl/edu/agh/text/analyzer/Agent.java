package pl.edu.agh.text.analyzer;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.edu.agh.corpus.provider.model.Post;
import pl.edu.agh.corpus.provider.model.Tag;

public class Agent implements Runnable {

	private CentralAgent centralAgent;
	private List<Post> posts;

	public Agent(CentralAgent centralAgent, List<Post> posts) {
		this.centralAgent = centralAgent;
		this.posts = posts;
	}

	@Override
	public void run() {
		System.out.println("[Agent] Starting... thread: " + Thread.currentThread());

		Result result = null;

		try {
			result = classify(posts);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}

		centralAgent.submitResults(result);
	}

	private Result classify(List<Post> posts) throws IOException, ParserConfigurationException, SAXException {
		System.out.println("[Agent] Classifying...");

		Classifier classifier = new Classifier();
		Result result = new Result();

		for (Post post : posts) {
			String text = post.getText();
			Tag tag = classifier.classify(text);
			post.setTag(tag);
			result.addPost(post);
		}
		return result;
	}
}
