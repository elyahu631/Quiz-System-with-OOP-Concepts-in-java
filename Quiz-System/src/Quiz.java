import java.util.List;

public record Quiz(List<IQuestion> questions) implements IQuiz {}
