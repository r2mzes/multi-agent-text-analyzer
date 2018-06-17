package pl.edu.agh.text.analyzer;

import java.io.*;
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

	Date earliestDate = new Date(Long.MAX_VALUE);
	Date latestDate = new Date(Long.MIN_VALUE);

	int postCounter = 0;

	public void addPost(Post post) {
		if (post != null) {
			postCounter++;
			countries.add(post.getThread().getCountry());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
			LocalDate localDate = LocalDate.parse(post.getPublished(), formatter);
			Date publishedDate = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

			if (publishedDate.before(earliestDate))
				earliestDate = publishedDate;
			if (publishedDate.after(latestDate))
				latestDate = publishedDate;

			Tag tag = post.getTag();
			if (tag != null) {
				updateStatistics(tag.getFirstLevelTag(), publishedDate, firstLevelTagsAmountPerDate);
				updateStatistics(tag.getSecondLevelTag(), publishedDate, secondLevelTagsAmountPerDate);
				updateStatistics(tag.getThirdLevelTag(), publishedDate, thirdevelTagsAmountPerDate);
			}
		}

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

	public void store() throws IOException {
		Date middlePoint = new Date((latestDate.getTime() - earliestDate.getTime()) / 2 + earliestDate.getTime());
		System.out.println(latestDate + " " + earliestDate + " " + middlePoint);
		String fileName = "";
		fileName += postCounter;
		for (String country : this.countries) {
			fileName += "-" + country;
		}
		File yourFile = new File("results/" + fileName + ".txt");
		yourFile.createNewFile();
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(yourFile, false), "utf-8"))) {
			writer.write(fileName);
			((BufferedWriter) writer).newLine();
			writer.write("posts amount: " + postCounter);
			((BufferedWriter) writer).newLine();
			writer.write("earliest date: " + earliestDate);
			((BufferedWriter) writer).newLine();
			writer.write("latest date: " + latestDate);
			((BufferedWriter) writer).newLine();
			writer.write("middle point date: " + middlePoint);
			((BufferedWriter) writer).newLine();
			((BufferedWriter) writer).newLine();

			Map<String, Integer> oldFirst = new HashMap<>();
			Map<String, Integer> currentFirst = new HashMap<>();

			int counter = 0;

			for (Map.Entry<Date, Map<String, Integer>> entry : firstLevelTagsAmountPerDate.entrySet()) {
				entry.getKey();
				if (entry.getKey().before(middlePoint) || entry.getKey().equals(middlePoint)) {
					for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
						Integer amount = oldFirst.get(entry2.getKey());
						if (amount == null) {
							amount = 0;
						}
						amount += entry2.getValue();
						oldFirst.put(entry2.getKey(), amount);
					}
				} else {
					if (counter < 450) {
						counter++;
						for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
							Integer amount = currentFirst.get(entry2.getKey());
							if (amount == null) {
								amount = 0;
							}
							amount += entry2.getValue();
							currentFirst.put(entry2.getKey(), amount);
						}
					}
				}
			}

			Map<String, Integer> oldSecond = new HashMap<>();
			Map<String, Integer> currentSecond = new HashMap<>();

			counter = 0;

			for (Map.Entry<Date, Map<String, Integer>> entry : secondLevelTagsAmountPerDate.entrySet()) {
				entry.getKey();
				if (entry.getKey().before(middlePoint) || entry.getKey().equals(middlePoint)) {
					for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
						Integer amount = oldSecond.get(entry2.getKey());
						if (amount == null) {
							amount = 0;
						}
						amount += entry2.getValue();
						oldSecond.put(entry2.getKey(), amount);
					}
				} else {
					if (counter < 450) {
						for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
							Integer amount = currentSecond.get(entry2.getKey());
							if (amount == null) {
								amount = 0;
							}
							amount += entry2.getValue();
							currentSecond.put(entry2.getKey(), amount);
						}
					}
				}
			}

			Map<String, Integer> oldThird = new HashMap<>();
			Map<String, Integer> currentThird = new HashMap<>();

			counter = 0;

			for (Map.Entry<Date, Map<String, Integer>> entry : thirdevelTagsAmountPerDate.entrySet()) {
				entry.getKey();
				if (entry.getKey().before(middlePoint) || entry.getKey().equals(middlePoint)) {
					for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
						Integer amount = oldThird.get(entry2.getKey());
						if (amount == null) {
							amount = 0;
						}
						amount += entry2.getValue();
						oldThird.put(entry2.getKey(), amount);
					}
				} else {
					if (counter < 450) {
						for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
							Integer amount = currentThird.get(entry2.getKey());
							if (amount == null) {
								amount = 0;
							}
							amount += entry2.getValue();
							currentThird.put(entry2.getKey(), amount);
						}
					}
				}
			}

			writer.write("PREVIOUS PERIOD");
			((BufferedWriter) writer).newLine();
			((BufferedWriter) writer).newLine();

			writer.write("FIRST LEVEL CATEGORIES");
			((BufferedWriter) writer).newLine();
			for (Map.Entry<String, Integer> entry : oldFirst.entrySet()) {
				writer.write(entry.getKey() + " " + entry.getValue());
				((BufferedWriter) writer).newLine();
			}

			((BufferedWriter) writer).newLine();
			writer.write("SECOND LEVEL CATEGORIES");
			((BufferedWriter) writer).newLine();
			for (Map.Entry<String, Integer> entry : oldSecond.entrySet()) {
				writer.write(entry.getKey() + " " + entry.getValue());
				((BufferedWriter) writer).newLine();
			}

			((BufferedWriter) writer).newLine();
			writer.write("THIRD LEVEL CATEGORIES");
			((BufferedWriter) writer).newLine();
			for (Map.Entry<String, Integer> entry : oldThird.entrySet()) {
				writer.write(entry.getKey() + " " + entry.getValue());
				((BufferedWriter) writer).newLine();
			}

			((BufferedWriter) writer).newLine();
			((BufferedWriter) writer).newLine();
			writer.write("CURRENT PERIOD");
			((BufferedWriter) writer).newLine();
			((BufferedWriter) writer).newLine();

			writer.write("FIRST LEVEL CATEGORIES");
			((BufferedWriter) writer).newLine();
			for (Map.Entry<String, Integer> entry : currentFirst.entrySet()) {
				writer.write(entry.getKey() + " " + entry.getValue());
				((BufferedWriter) writer).newLine();
			}

			((BufferedWriter) writer).newLine();
			writer.write("SECOND LEVEL CATEGORIES");
			((BufferedWriter) writer).newLine();
			for (Map.Entry<String, Integer> entry : currentSecond.entrySet()) {
				writer.write(entry.getKey() + " " + entry.getValue());
				((BufferedWriter) writer).newLine();
			}

			((BufferedWriter) writer).newLine();
			writer.write("THIRD LEVEL CATEGORIES");
			((BufferedWriter) writer).newLine();
			for (Map.Entry<String, Integer> entry : currentThird.entrySet()) {
				writer.write(entry.getKey() + " " + entry.getValue());
				((BufferedWriter) writer).newLine();
			}

		}
	}

}
