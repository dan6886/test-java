package time;


/**
 * @author Mr.Paper
 * @date 2018-05-08 10:19
 **/
public class NlpPeriodTimeConfig {

    private String id;
    private String word;
    private String type;
    private String startMonthDate;
    private String endMonthDate;
    private int calculateNumber;

    public NlpPeriodTimeConfig() {
    }

    public NlpPeriodTimeConfig(String word, String type, String startMonthDate, String endMonthDate, int calculateNumber) {
        this.word = word;
        this.type = type;
        this.startMonthDate = startMonthDate;
        this.endMonthDate = endMonthDate;
        this.calculateNumber = calculateNumber;
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

    public String getStartMonthDate() {
        return startMonthDate;
    }

    public void setStartMonthDate(String startMonthDate) {
        this.startMonthDate = startMonthDate;
    }

    public String getEndMonthDate() {
        return endMonthDate;
    }

    public void setEndMonthDate(String endMonthDate) {
        this.endMonthDate = endMonthDate;
    }

    public int getCalculateNumber() {
        return calculateNumber;
    }

    public void setCalculateNumber(int calculateNumber) {
        this.calculateNumber = calculateNumber;
    }
}
