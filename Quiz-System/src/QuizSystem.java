import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class QuizSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Integer> globalScores = new HashMap<>();

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

    private static void startQuiz() {
        System.out.print("Enter your name: ");
        String name = scanner.next();
        QuizTaker quizTaker = new QuizTaker(name, scanner);
        System.out.print("Enter the number of questions for the quiz: ");
        int numQuestions = scanner.nextInt();
        IQuiz quiz = new Quiz(generateRandomQuiz(numQuestions));
        int score = quizTaker.takeQuiz(quiz);
        globalScores.put(quizTaker.getName(), score);
    }

    private static List<IQuestion> generateRandomQuiz(int numberOfQuestions) {
        List<IQuestion> allQuestions = populateQuestions();
        Collections.shuffle(allQuestions);
        return allQuestions.subList(0, Math.min(numberOfQuestions, allQuestions.size()));
    }

    private static void displayResults() {
        for (Map.Entry<String, Integer> entry : globalScores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void saveResults() {
        try (PrintWriter out = new PrintWriter("quiz_results.txt")) {
            for (Map.Entry<String, Integer> entry : globalScores.entrySet()) {
                out.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not save results: " + e.getMessage());
        }
    }

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
