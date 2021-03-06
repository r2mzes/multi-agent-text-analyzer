package pl.edu.agh.text.analyzer;

import java.util.HashSet;
import java.util.Set;

public class CentralAgent implements Runnable {

	private volatile boolean running;
	private Set<Result> results = new HashSet<>();
	private int centralPostCaounter = 0;

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
		System.out.println("[Central Agent] Stopping...");
		running = false;
	}

	/**
	 * This method should be used by agents to submit results of their work. It
	 * simply takes a map, which contains a set of countries as a key, for which
	 * posts where processed and set of tags found for these posts as a value
	 * 
	 * @param result
	 *            Set<Result>
	 */
	public synchronized void submitResults(Result result) {
		System.out.println("[Central Agent] Results received");
		results.add(result);
		centralPostCaounter += result.postCounter;
		System.out.println(centralPostCaounter);
	}
}
