package pl.edu.agh.corpus.provider.model;

public class Tag {

	String firstLevelTag;
	String secondLevelTag;
	String thirdLevelTag;

	public Tag(String firstLevelTag, String secondLevelTag, String thirdLevelTag) {
		this.firstLevelTag = firstLevelTag;
		this.secondLevelTag = secondLevelTag;
		this.thirdLevelTag = thirdLevelTag;
	}

    public String getFirstLevelTag() {
        return firstLevelTag;
    }

    public String getSecondLevelTag() {
        return secondLevelTag;
    }

    public String getThirdLevelTag() {
        return thirdLevelTag;
    }
}
