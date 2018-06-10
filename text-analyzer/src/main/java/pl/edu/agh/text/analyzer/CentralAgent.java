package pl.edu.agh.text.analyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CentralAgent implements Runnable {

	private volatile boolean running;
	private Map<Set<String>, Set<String>> tagsPerCountryGroup = new HashMap<>();

	@Override
	public void run() {
		System.out.println("[Central Agent] Starting.... thread: " + Thread.currentThread());
		running = true;
		while (running) {
			if (Thread.interrupted()) {
				return;
			}
		}
	}

	public void stop() {
		System.out.println("[Central Agent] Stopping");
		running = false;
	}

	/**
	 * This method should be used by agents to submit results of their work. It
	 * simply takes a map, which contains a set of countries as a key, for which
	 * posts where processed and set of tags found for these posts as a value
	 * 
	 * @param results
	 *            Map<Set<Country>, Set<Tag>>
	 */
	public synchronized void submitResults(Map<Set<String>, Set<String>> results) {
		System.out.println("[Central Agent] results received (size = " + results.size() + ")");
		tagsPerCountryGroup.putAll(results);
	}
}
