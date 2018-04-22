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
		WebhoseIOClient webhoseClient = WebhoseIOClient.getInstance("5db90f02-b5e6-41da-bfc4-45eed26b0e99");

		Map<String, String> queries = new HashMap<String, String>();
		queries.put("q", "language:english");
		queries.put("sort", "crawled");

		JsonObject outputJSON = new JsonObject();

		JsonArray  threads = new JsonArray();

		JsonElement result = webhoseClient.query("filterWebContent", queries);

		for(int i=0; i<900;i++){
			result = webhoseClient.getNext();
			threads.addAll(result.getAsJsonObject().getAsJsonArray("posts"));
		}
		outputJSON.add("posts", threads);
		try (Writer writer = new FileWriter("corpus.json")) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(outputJSON, writer);
		}
		System.out.println(result.getAsJsonObject().get("totalResults")); // Print posts count
	}
}
