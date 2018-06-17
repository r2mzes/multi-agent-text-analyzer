package pl.edu.agh.text.analyzer;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pl.edu.agh.corpus.provider.CorpusProvider;
import pl.edu.agh.corpus.provider.model.Post;

public class TextAnalyzer {

	private static final int AMOUNT_OF_COUNTRIES_IN_GROUP = 1;

	private CorpusProvider corpusProvider;
	private String outputDirectory;

	public TextAnalyzer(CorpusProvider corpusProvider, String outputDirectory) {
		this.corpusProvider = corpusProvider;
		this.outputDirectory = outputDirectory;
	}

	public void analyze() throws FileNotFoundException, InterruptedException {
		Map<Set<String>, List<Post>> postsByCountryGroup = corpusProvider
				.getPostsByCountryGroup(AMOUNT_OF_COUNTRIES_IN_GROUP);

		CentralAgent centralAgent = new CentralAgent();
		Thread centralAgentThread = new Thread(centralAgent);
		centralAgentThread.start();

		int availableThreads = 4;
		System.out.println("[TextAnalyzer] Creating thread pool of a size: " + availableThreads);
		ExecutorService executor = Executors.newFixedThreadPool(availableThreads);

		for (Map.Entry<Set<String>, List<Post>> entry : postsByCountryGroup.entrySet()) {
			executor.execute(new Agent(centralAgent, entry.getValue(),
					entry.getKey().contains("AU") && entry.getKey().contains("AT")));
		}

		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		centralAgent.stop();
	}
}
