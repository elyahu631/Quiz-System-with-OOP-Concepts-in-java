import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Allows a user to take a quiz by displaying questions, accepting answers, and
 * calculating the score.
 */
public class QuizTaker {
    private final String name;
    private final Scanner scanner;


    public QuizTaker(String name, Scanner scanner) {
        this.name = name;
        this.scanner = scanner;
    }

    /**
     * The function takes a quiz object, displays each question, waits for the user to input an answer,
     * checks if the answer is correct, keeps track of the score, and returns the final score.
     *
     * @param quiz The parameter "quiz" is of type IQuiz, which is an interface representing a quiz. It
     * contains a list of questions (of type IQuestion) that the user will be asked during the quiz.
     * @return The method is returning an integer, which represents the score achieved by the user in
     * the quiz.
     */
    public int takeQuiz(IQuiz quiz) {
        long startTime = System.currentTimeMillis();
        int score = 0;
        int totalQuizTime = quiz.questions().size() * 10_000; // 10 seconds per question
        boolean completedAllQuestions = true;
        List<IQuestion> wrongQuestions = new ArrayList<>();

        for (IQuestion question : quiz.questions()) {
            if (System.currentTimeMillis() - startTime > totalQuizTime) {
                System.out.println("Time's up for the quiz!");
                completedAllQuestions = false;
                break;
            }

            question.displayQuestion();
            System.out.print("Enter your answer (index): ");
            while (System.currentTimeMillis() - startTime < totalQuizTime) {
                try {
                    if (System.in.available() > 0) {
                        int answerIndex = scanner.nextInt();
                        if (!question.isCorrectAnswer(answerIndex)) {
                            wrongQuestions.add(question);
                        } else {
                            score++;
                        }
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // to clear the input
                } catch (IOException e) {
                    System.out.println("An error occurred while reading the input.");
                }
            }
        }

        long endTime = System.currentTimeMillis();
        if (completedAllQuestions) {
            displayCompletionMessage(startTime, endTime);
            displayCorrectAnswersForWrongQuestions(wrongQuestions);
        }
        System.out.println("Your score: " + score);
        return score;
    }

    /**
     * The function displays the duration of a quiz in seconds.
     *
     * @param startTime The starting time of the quiz, typically recorded using the
     * System.currentTimeMillis() method.
     * @param endTime The endTime parameter represents the time when the quiz was completed.
     */
    private void displayCompletionMessage(long startTime, long endTime) {
        long duration = endTime - startTime;
        System.out.println("Quiz completed in: " + duration / 1000 + " seconds");
    }

    /**
     * The function displays the correct answers for the questions that were answered incorrectly.
     *
     * @param wrongQuestions A list of IQuestion objects representing the questions that the user got
     * wrong.
     */
    private void displayCorrectAnswersForWrongQuestions(List<IQuestion> wrongQuestions) {
        if (!wrongQuestions.isEmpty()) {
            System.out.println("\nCorrect Answers for the Questions You Got Wrong:");
            wrongQuestions.forEach(IQuestion::displayCorrectAnswer);
        }
    }

    public String getName() {
        return name;
    }
}
