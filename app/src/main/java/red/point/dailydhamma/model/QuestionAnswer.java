package red.point.dailydhamma.model;

public class QuestionAnswer {
    private String title;
    private String question;
    private String answer;
    private String key;

    public QuestionAnswer() {}

    public QuestionAnswer(String title, String question, String answer) {
        this.title = title;
        this.question = question;
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
