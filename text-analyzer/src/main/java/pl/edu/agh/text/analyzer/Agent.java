package pl.edu.agh.text.analyzer;

import java.io.IOException;
import java.util.*;

import org.xml.sax.SAXException;
import pl.edu.agh.corpus.provider.model.Post;

import javax.xml.parsers.ParserConfigurationException;

public class Agent implements Runnable {

    private CentralAgent centralAgent;
    private List<Post> posts;

    public Agent(CentralAgent centralAgent, List<Post> posts) {
        this.centralAgent = centralAgent;
        this.posts = posts;
    }

    @Override
    public void run() {
        System.out.println("[Agent] Starting... thread: " + Thread.currentThread());
        Map<Set<String>, Set<String>> tagsPerCountryGroup = null;

        try {
            tagsPerCountryGroup = classify(posts);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        centralAgent.submitResults(tagsPerCountryGroup);
    }

    private Map<Set<String>, Set<String>> classify(List<Post> posts) throws IOException, ParserConfigurationException, SAXException {
        System.out.println("[Agent] Classifying...");

        Classifier classifier = new Classifier();
        Map<Set<String>, Set<String>> collection = new HashMap<>();
        Set<String> countries = new HashSet<>();
        Set<String> labels = new HashSet<>();

        for (Post post : posts) {
            String country = post.getThread().getCountry();
            String text = post.getText();

            String label = classifier.classify(text);
            if (label.equals("")) continue;

            countries.add(country);
            labels.add(label);
        }

        collection.put(countries, labels);
        return collection;
    }
}
