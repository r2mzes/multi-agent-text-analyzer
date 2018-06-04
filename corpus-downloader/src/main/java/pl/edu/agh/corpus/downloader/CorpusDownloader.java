package pl.edu.agh.corpus.downloader;

import com.google.gson.*;
import pl.edu.agh.corpus.downloader.webhose.WebhoseIOClient;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class CorpusDownloader {

	public void getNewsTaggedWithCountryOfOrigin() throws IOException, URISyntaxException {
		WebhoseIOClient webhoseClient = WebhoseIOClient.getInstance("4a94baec-ff68-4c73-9df1-ae813fae2624");

		Map<String, String> queries = new HashMap<String, String>();
		queries.put("q", "site_type:news language:english");
		queries.put("sort", "crawled");

		JsonElement result = webhoseClient.query("filterWebContent", queries);

		for (int i = 0; i < 900; i++) {
			result = webhoseClient.getNext();
			try (Writer writer = new FileWriter("news_corpus/corpus" + "_" + i + ".json")) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				gson.toJson(result, writer);
			}
		}

		System.out.println(result.getAsJsonObject().get("totalResults")); // Print posts count
	}
}
