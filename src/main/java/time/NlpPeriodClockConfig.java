package time;

/**
 * @author Mr.Paper
 * @date 2018-06-13 17:08
 **/
public class NlpPeriodClockConfig {
    private String id;
    private String word;
    private String type;
    private String start;
    private String end;

    public NlpPeriodClockConfig() {
    }

    public NlpPeriodClockConfig(String word, String type, String start, String end) {
        this.word = word;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
