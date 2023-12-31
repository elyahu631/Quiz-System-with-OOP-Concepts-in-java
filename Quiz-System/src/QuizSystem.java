import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * QuizSystem class is the main class of the Quiz System application.
 * It contains the main method, which initializes the application and
 * controls the flow of the application.
 */
public class QuizSystem {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Stores the scores of all the quiz takers. The key is
     * the name of the quiz taker, and the value is the score.
     */
    private static final Map<String, Integer> globalScores = new HashMap<>();

    /**
     * main method is the entry point of the Quiz System application. It
     * initializes the application and starts the main menu loop.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        loadResults();
        while (true) {
            System.out.println("\n1. Start Quiz");
            System.out.println("2. Display Results");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    startQuiz();
                    saveResults();
                    break;
                case 2:
                    displayResults();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Starts a new quiz and records the score of the quiz taker.     
     */
    private static void startQuiz() {
        System.out.print("Enter your name: ");
        String name = scanner.next();
        QuizTaker quizTaker = new QuizTaker(name, scanner);
        System.out.print("Enter the number of questions for the quiz: ");
        int numQuestions = scanner.nextInt();
        System.out.print("\n ### "+ name + " WELCOME To Quiz Master ###\n ----------------------------------- \n");
        IQuiz quiz = new Quiz(generateRandomQuiz(numQuestions));
        int score = quizTaker.takeQuiz(quiz);
        globalScores.put(quizTaker.getName(), score);
    }

    /**
     * Generates a random quiz with the specified number of questions.
     *
     * @param numberOfQuestions number of questions in the quiz
     * @return a list of questions
     */
    private static List<IQuestion> generateRandomQuiz(int numberOfQuestions) {
        List<IQuestion> allQuestions = populateQuestions();
        Collections.shuffle(allQuestions);
        return allQuestions.subList(0, Math.min(numberOfQuestions, allQuestions.size()));
    }

    /**
     * Displays the scores of all the quiz takers.
     */
    private static void displayResults() {
        for (Map.Entry<String, Integer> entry : globalScores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * sSaves the scores of all the quiz takers to a file.
     */
    private static void saveResults() {
        try (PrintWriter out = new PrintWriter("quiz_results.txt")) {
            for (Map.Entry<String, Integer> entry : globalScores.entrySet()) {
                out.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not save results: " + e.getMessage());
        }
    }

    /**
     * Loads the scores of all the quiz takers from a file.
     */
    private static void loadResults() {
        try (Scanner fileScanner = new Scanner(new File("quiz_results.txt"))) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(":");
                globalScores.put(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous results found.");
        }
    }

    /**
     * Loads the questions from a file and returns a list of questions.
     *
     * @return a list of questions
     */
    private static List<IQuestion> populateQuestions() {
        List<IQuestion> questions = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File("questions.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length < 2) {
                    continue; // Skip improperly formatted lines
                }

                String questionType = parts[0];
                String questionText = parts[1];

                if ("MC".equals(questionType) && parts.length >= 4) {
                    // Multiple-choice question
                    List<String> options = Arrays.asList(Arrays.copyOfRange(parts, 2, parts.length - 1));
                    int correctOptionIndex = Integer.parseInt(parts[parts.length - 1]);
                    questions.add(new Question(questionText, options, correctOptionIndex));
                } else if ("TF".equals(questionType)) {
                    // True/False question
                    boolean correctAnswer = Boolean.parseBoolean(parts[2]);
                    questions.add(new TrueFalseQuestion(questionText, correctAnswer));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Questions file not found.");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing the correct option index.");
        }
        return questions;
    }
}
