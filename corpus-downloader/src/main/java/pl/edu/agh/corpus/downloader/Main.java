package pl.edu.agh.corpus.downloader;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

	public static void main(String[] args) throws IOException, URISyntaxException {
		CorpusDownloader corpusDownloader = new CorpusDownloader();
		corpusDownloader.getNewsTaggedWithCountryOfOrigin();
	}
}
