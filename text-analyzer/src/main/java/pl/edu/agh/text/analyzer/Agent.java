package pl.edu.agh.text.analyzer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.corpus.provider.model.Post;

public class Agent implements Runnable {

	private CentralAgent centralAgent;
	private List<Post> posts;

	public Agent(CentralAgent centralAgent, List<Post> posts) {
		this.centralAgent = centralAgent;
		this.posts = posts;
	}

	@Override
	public void run() {
		Map<Set<String>, Set<String>> tagsPerCountryGroup = classify(posts);
		centralAgent.submitResults(tagsPerCountryGroup);
	}

	private Map<Set<String>, Set<String>> classify(List<Post> posts) {
		// TODO add implementation here, store results in tags
		return null;
	}
}
