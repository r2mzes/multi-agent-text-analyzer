package pl.edu.agh.corpus.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import pl.edu.agh.corpus.provider.model.Post;
import pl.edu.agh.corpus.provider.model.Thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CorpusProvider {
	private static final String PATH_TO_CORPUS = "corpus";

	public List<Post> getPosts() throws FileNotFoundException {
		List<Post> posts = new ArrayList<>();

		File dir = new File(PATH_TO_CORPUS);
		File[] directoryListing = dir.listFiles();
		Type listType = new TypeToken<List<Post>>() {
		}.getType();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				Gson gson = new Gson();
				JsonReader reader = new JsonReader(new FileReader(child));
				JsonObject object = gson.fromJson(reader, JsonObject.class);
				JsonArray postJsonArray = object.getAsJsonArray("posts");
				posts = gson.fromJson(postJsonArray.toString(), listType);
			}
		}

		return posts;
	}

	public static void main(String[] args) throws FileNotFoundException {
		CorpusProvider app = new CorpusProvider();
		app.getPosts();
	}
}
