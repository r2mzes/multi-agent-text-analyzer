package pl.edu.agh.corpus.provider.model;

import java.util.List;

public class Thread {

	private String uuid, url, site, site_full, site_section, section_title, title, title_full, published, site_type,
			country, main_image;
	private List<String> site_categories;
	private double replies_count, participants_count, spam_score, performance_score, domain_rank;

    public String getUuid() {
        return uuid;
    }

    public String getUrl() {
        return url;
    }

    public String getSite() {
        return site;
    }

    public String getSite_full() {
        return site_full;
    }

    public String getSite_section() {
        return site_section;
    }

    public String getSection_title() {
        return section_title;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_full() {
        return title_full;
    }

    public String getPublished() {
        return published;
    }

    public String getSite_type() {
        return site_type;
    }

    public String getCountry() {
        return country;
    }

    public String getMain_image() {
        return main_image;
    }

    public List<String> getSite_categories() {
        return site_categories;
    }

    public double getReplies_count() {
        return replies_count;
    }

    public double getParticipants_count() {
        return participants_count;
    }

    public double getSpam_score() {
        return spam_score;
    }

    public double getPerformance_score() {
        return performance_score;
    }

    public double getDomain_rank() {
        return domain_rank;
    }
}
