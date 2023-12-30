import java.util.List;

/**
 The `IQuiz` interface defines a contract for a quiz. It requires implementing classes to provide a
 method `questions()` that returns a list of `IQuestion` objects. This allows different types of
 quizzes to be created, each with their own set of questions.
 */

public interface IQuiz {
    List<IQuestion> questions();
}