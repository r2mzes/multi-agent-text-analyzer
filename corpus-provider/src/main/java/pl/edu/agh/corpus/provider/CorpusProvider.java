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
import pl.edu.agh.corpus.provider.model.Thread;

public class CorpusProvider {
	private static final String DEFAULT_PATH_TO_CORPUS = "news_corpus";

	private String pathToCorpus;

	public CorpusProvider() {
		this.pathToCorpus = DEFAULT_PATH_TO_CORPUS;
	}

	public CorpusProvider(String pathToCorpus) {
		this.pathToCorpus = pathToCorpus;
	}

	public List<Post> getPosts() throws FileNotFoundException {
		List<Post> posts = new ArrayList<>();

		File dir = new File(DEFAULT_PATH_TO_CORPUS);
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

	public Map<Set<String>, List<Post>> getPostsByCountryGroup(int countryAmountPerGroup) throws FileNotFoundException {
		List<Post> allPosts = getPosts();
		Set<String> allCountries = allPosts.stream().map(Post::getThread).map(Thread::getCountry).distinct()
				.collect(Collectors.toSet());

		Set<Set<String>> allCountryPowerSet = powerSet(allCountries);
		Set<Set<String>> allCountryCombinations = allCountryPowerSet.stream()
				.filter(set -> set.size() == countryAmountPerGroup).collect(Collectors.toSet());

		Map<Set<String>, List<Post>> postsByCountryGroup = new HashMap<>();

		for (Set<String> countryGroup : allCountryCombinations) {
			postsByCountryGroup.put(countryGroup, allPosts.stream()
					.filter(post -> countryGroup.contains(post.getThread().getCountry())).collect(Collectors.toList()));
		}
		return postsByCountryGroup;
	}

	public <T> Set<Set<T>> powerSet(Set<T> originalSet) {
		Set<Set<T>> sets = new LinkedHashSet<Set<T>>();
		if (originalSet.isEmpty()) {
			sets.add(new LinkedHashSet<T>());
			return sets;
		}
		List<T> list = new ArrayList<T>(originalSet);
		T head = list.get(0);
		Set<T> rest = new LinkedHashSet<T>(list.subList(1, list.size()));
		for (Set<T> set : powerSet(rest)) {
			Set<T> newSet = new LinkedHashSet<T>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

	public static void main(String[] args) throws FileNotFoundException {
		CorpusProvider app = new CorpusProvider();
		app.getPostsByCountryGroup(3);
		System.out.println("DONE");
	}
}
