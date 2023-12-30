import java.util.Arrays;

/**
 * Represents a true or false question with a given question text and
 * correct answer.
 */
public class TrueFalseQuestion extends Question {
    public TrueFalseQuestion(String questionText, boolean correctAnswer) {
        super(questionText, Arrays.asList("True", "False"), correctAnswer ? 0 : 1);
    }
}