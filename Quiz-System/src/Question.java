import java.util.List;

/**
 * Represents a question with multiple options and provides methods to display the
 * question, check if an answer is correct, and display the correct answer.
 */
public class Question implements IQuestion {
    private final String questionText;
    private final List<String> options;
    private final int correctOptionIndex;

    public Question(String questionText, List<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    // Displays the question and its options
    @Override
    public void displayQuestion() {
        System.out.println(questionText);
        for (int i = 0; i < options.size(); i++) {
            System.out.println(i + ". " + options.get(i));
        }
    }

    // Checks if the given answer index is correct
    @Override
    public boolean isCorrectAnswer(int answerIndex) {
        return answerIndex == correctOptionIndex;
    }

    // Displays the correct answer
    @Override
    public void displayCorrectAnswer() {
        System.out.println("Correct answer: \u001B[32m" + options.get(correctOptionIndex) + "\u001B[0m");
    }
}