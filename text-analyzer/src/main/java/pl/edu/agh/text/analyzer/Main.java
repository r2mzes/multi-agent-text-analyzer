package pl.edu.agh.text.analyzer;

import java.io.FileNotFoundException;

import pl.edu.agh.corpus.provider.CorpusProvider;

public class Main {

	static final String PATH_TO_CORPUS = "news_corpus";
	static final String OUTPUT_DIRECTORY_PATH = "results";

	public static void main(String[] args) throws FileNotFoundException {
		CorpusProvider corpusProvider = new CorpusProvider(PATH_TO_CORPUS);
		TextAnalyzer textAnalyzer = new TextAnalyzer(corpusProvider, OUTPUT_DIRECTORY_PATH);
		textAnalyzer.analyze();
	}
}
