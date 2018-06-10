package pl.edu.agh.text.analyzer;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.edu.agh.corpus.provider.CorpusProvider;
import pl.edu.agh.corpus.provider.model.Post;

public class TextAnalyzer {

	private static final int AMOUNT_OF_COUNTRIES_IN_GROUP = 3;

	private CorpusProvider corpusProvider;
	private String outputDirectory;

	public TextAnalyzer(CorpusProvider corpusProvider, String outputDirectory) {
		this.corpusProvider = corpusProvider;
		this.outputDirectory = outputDirectory;
	}

	public void analyze() throws FileNotFoundException {
		Map<Set<String>, List<Post>> postsByCountryGroup =  corpusProvider.getPostsByCountryGroup(AMOUNT_OF_COUNTRIES_IN_GROUP);

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for(List<Post> posts : postsByCountryGroup.values()){

		}

	}
}
