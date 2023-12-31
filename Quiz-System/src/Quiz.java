import java.util.List;

/**
 * A Quiz is a collection of questions.
 * 
 * @param questions a list of questions
 */
public record Quiz(List<IQuestion> questions) implements IQuiz {}