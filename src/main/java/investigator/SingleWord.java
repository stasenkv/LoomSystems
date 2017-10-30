package investigator;

import java.util.Date;

/**
 * Created by stasenkv on 10/29/2017.
 */
public class SingleWord {
    private String word;
    private Date date;

    public SingleWord(String word, Date date) {
        this.word = word;
        this.date = date;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SingleWord{" +
                "word='" + word + '\'' +
                ", date=" + date +
                '}';
    }
}
