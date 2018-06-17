package pl.edu.agh.corpus.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import pl.edu.agh.corpus.provider.model.Post;

public class CorpusProvider {
	private static final String DEFAULT_PATH_TO_CORPUS = "news_corpus_full";

	private String pathToCorpus;

	public CorpusProvider() {
		this.pathToCorpus = DEFAULT_PATH_TO_CORPUS;
	}

	public CorpusProvider(String pathToCorpus) {
		this.pathToCorpus = pathToCorpus;
	}

	public List<Post> getPosts() throws FileNotFoundException {
		List<Post> posts = new ArrayList<>();

		File dir = new File(pathToCorpus);
		File[] directoryListing = dir.listFiles();
		Type listType = new TypeToken<List<Post>>() {
		}.getType();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				Gson gson = new Gson();
				JsonReader reader = new JsonReader(new FileReader(child));
				JsonObject object = gson.fromJson(reader, JsonObject.class);
				JsonArray postJsonArray = object.getAsJsonArray("posts");
				posts.addAll(gson.fromJson(postJsonArray.toString(), listType));
			}
		}

		return posts;
	}

	public Map<Set<String>, List<Post>> getPostsByCountryGroup(int countryAmountPerGroup) throws FileNotFoundException {
		List<Post> allPosts = getPosts();
		System.out.println("Amount of all posts: " + allPosts.size());

		Set<String> allCountries = new HashSet<>();
		for (Post post : allPosts) {
			allCountries.add(post.getThread().getCountry());
		}

		Set<Set<String>> allCountryCombinations = new HashSet<>();
		Iterator<String> iterator = allCountries.iterator();
		String previous = iterator.next();
		while (iterator.hasNext()) {
			String current = iterator.next();
			Set<String> subGroup = new HashSet<>();
			subGroup.add(previous);
			subGroup.add(current);
			allCountryCombinations.add(subGroup);
			previous = current;
		}

		System.out.println("Countries combination computed");

		Map<Set<String>, List<Post>> postsByCountryGroup = new HashMap<>();

		System.out.println("Adding posts to country groups");
		for (Set<String> countryGroup : allCountryCombinations) {
			postsByCountryGroup.put(countryGroup, allPosts.stream()
					.filter(post -> countryGroup.contains(post.getThread().getCountry())).collect(Collectors.toList()));
		}
		return postsByCountryGroup;
	}

	public static void main(String[] args) throws FileNotFoundException {
		CorpusProvider app = new CorpusProvider();
		app.getPostsByCountryGroup(3);
		System.out.println("DONE");
	}
}
