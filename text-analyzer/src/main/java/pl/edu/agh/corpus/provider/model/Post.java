package pl.edu.agh.corpus.provider.model;

import java.util.List;

public class Post {

	private Thread thread;
	private String uuid, url, ord_in_thread, author, published, title, text, highlitText, highlightTitle, language;
	private List<String> external_links;

	public Thread getThread() {
		return thread;
	}

	public Post setThread(Thread thread) {
		this.thread = thread;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public Post setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Post setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getOrd_in_thread() {
		return ord_in_thread;
	}

	public Post setOrd_in_thread(String ord_in_thread) {
		this.ord_in_thread = ord_in_thread;
		return this;
	}

	public String getAuthor() {
		return author;
	}

	public Post setAuthor(String author) {
		this.author = author;
		return this;
	}

	public String getPublished() {
		return published;
	}

	public Post setPublished(String published) {
		this.published = published;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Post setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getText() {
		return text;
	}

	public Post setText(String text) {
		this.text = text;
		return this;
	}

	public String getHighlitText() {
		return highlitText;
	}

	public Post setHighlitText(String highlitText) {
		this.highlitText = highlitText;
		return this;
	}

	public String getHighlightTitle() {
		return highlightTitle;
	}

	public Post setHighlightTitle(String highlightTitle) {
		this.highlightTitle = highlightTitle;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public Post setLanguage(String language) {
		this.language = language;
		return this;
	}

	public List<String> getExternal_links() {
		return external_links;
	}

	public Post setExternal_links(List<String> external_links) {
		this.external_links = external_links;
		return this;
	}
}
