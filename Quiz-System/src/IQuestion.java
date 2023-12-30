
/**
 * The `IQuestion` interface defines a contract for a question. It requires implementing classes to provide
 * methods for displaying the question (`displayQuestion()`), checking if a given answer is correct
 * (`isCorrectAnswer(int answerIndex)`), and displaying the correct answer (`displayCorrectAnswer()`).
 * This allows for the creation of various types of questions with different display and validation logic.
 */
public interface IQuestion {
    void displayQuestion();
    boolean isCorrectAnswer(int answerIndex);
    void displayCorrectAnswer();

}