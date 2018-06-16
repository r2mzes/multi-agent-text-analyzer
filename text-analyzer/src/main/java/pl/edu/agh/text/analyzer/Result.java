package pl.edu.agh.text.analyzer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import pl.edu.agh.corpus.provider.model.Post;
import pl.edu.agh.corpus.provider.model.Tag;

public class Result {
	Set<String> countries = new HashSet<>();
	Map<Date, Map<String, Integer>> firstLevelTagsAmountPerDate = new HashMap<>();
	Map<Date, Map<String, Integer>> secondLevelTagsAmountPerDate = new HashMap<>();
	Map<Date, Map<String, Integer>> thirdevelTagsAmountPerDate = new HashMap<>();

	public void addPost(Post post) {
		countries.add(post.getThread().getCountry());
		Tag tag = post.getTag();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
		LocalDate localDate = LocalDate.parse(post.getPublished(), formatter);
		Date publishedDate = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

		updateStatistics(tag.getFirstLevelTag(), publishedDate, firstLevelTagsAmountPerDate);
		updateStatistics(tag.getSecondLevelTag(), publishedDate, secondLevelTagsAmountPerDate);
		updateStatistics(tag.getThirdLevelTag(), publishedDate, thirdevelTagsAmountPerDate);

	}

	private void updateStatistics(String tag, Date date, Map<Date, Map<String, Integer>> tagsAmountPerDate) {
		if (tag != null) {
			Map<String, Integer> tagsAmount = tagsAmountPerDate.get(date);
			if (tagsAmount == null) {
				tagsAmount = new HashMap<>();
				tagsAmountPerDate.put(date, tagsAmount);
			}
			Integer amount = tagsAmount.get(tag);
			if (amount == null) {
				amount = 0;
			}
			amount++;
			tagsAmount.put(tag, amount);
		}
	}

}
