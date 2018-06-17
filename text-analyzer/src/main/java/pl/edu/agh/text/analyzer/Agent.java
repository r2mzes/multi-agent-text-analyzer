package pl.edu.agh.text.analyzer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.edu.agh.corpus.provider.model.Post;
import pl.edu.agh.corpus.provider.model.Tag;

public class Agent implements Runnable {

	private CentralAgent centralAgent;
	private List<Post> posts;
	private boolean fullClassification;

	public Agent(CentralAgent centralAgent, List<Post> posts, boolean fullClassification) {
		this.centralAgent = centralAgent;
		this.posts = posts;
		this.fullClassification = fullClassification;
	}

	@Override
	public void run() {
		System.out.println("[Agent] Starting... thread: " + Thread.currentThread());

		Result result = null;

		try {
			result = classify(posts);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			result.store();
		} catch (IOException e) {
			e.printStackTrace();
		}
		centralAgent.submitResults(result);
	}

	private Result classify(List<Post> posts)
			throws IOException, ParserConfigurationException, SAXException, InterruptedException {
		System.out.println("[Agent] Classifying...");

		Classifier classifier = new Classifier();
		Result result = new Result();
		Map<Date, Integer> postAmountPerDate = new HashMap<>();
		for (Post post : posts) {
			String text = post.getTitle() + " " + post.getText();
			try {

				if (fullClassification) {
					 Thread.sleep(500);
					 Tag tag = classifier.classify(text);
					 System.out.println(tag.getFirstLevelTag() + " -> " + tag.getSecondLevelTag()
					 + " -> "
					 + tag.getThirdLevelTag());
					 post.setTag(tag);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
							Locale.ENGLISH);
					LocalDate localDate = LocalDate.parse(post.getPublished(), formatter);
					Date publishedDate = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
					Integer amount = postAmountPerDate.get(publishedDate);
					if (amount == null)
						amount = 0;
					amount++;
					postAmountPerDate.put(publishedDate, amount);
				}
				result.addPost(post);
			} catch (NullPointerException e) {
				System.err.println("null");
			}
		}
		if (fullClassification) {
			for (Map.Entry<Date, Integer> stat : postAmountPerDate.entrySet()) {
				System.err.println(stat.getKey() + " -> " + stat.getValue());
			}
		}
		return result;
	}
}
