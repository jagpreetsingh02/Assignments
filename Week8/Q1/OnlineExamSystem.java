import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class Question {
    private final int number;
    private final String text;

    protected Question(int number, String text) {
        this.number = number;
        this.text = text;
    }

    int getNumber() {
        return number;
    }

    String getText() {
        return text;
    }

    abstract boolean isValidAnswer(String answer);

    abstract boolean isCorrect(String answer);
}

class MultipleChoiceQuestion extends Question {
    private final String[] options;
    private final String correctOption;

    MultipleChoiceQuestion(int number, String text, String[] options, String correctOption) {
        super(number, text);
        this.options = options.clone();
        this.correctOption = correctOption;
    }

    @Override
    boolean isValidAnswer(String answer) {
        for (String option : options) {
            if (option.equalsIgnoreCase(answer)) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean isCorrect(String answer) {
        return correctOption.equalsIgnoreCase(answer);
    }
}

class TrueFalseQuestion extends Question {
    private final boolean correctValue;

    TrueFalseQuestion(int number, String text, boolean correctValue) {
        super(number, text);
        this.correctValue = correctValue;
    }

    @Override
    boolean isValidAnswer(String answer) {
        return answer.equalsIgnoreCase("true") || answer.equalsIgnoreCase("false");
    }

    @Override
    boolean isCorrect(String answer) {
        return Boolean.parseBoolean(answer) == correctValue;
    }
}

class Examination {
    private final String title;
    private final List<Question> questions = new ArrayList<>();

    Examination(String title) {
        this.title = title;
    }

    void addQuestion(Question q) {
        questions.add(q);
    }

    Question getQuestion(int number) {
        for (Question q : questions) {
            if (q.getNumber() == number) {
                return q;
            }
        }
        return null;
    }

    List<Question> getQuestions() {
        return questions;
    }

    String getTitle() {
        return title;
    }
}

enum AttemptStatus {
    IN_PROGRESS, SUBMITTED
}

class Result {
    private final int correct;
    private final int total;

    Result(int correct, int total) {
        this.correct = correct;
        this.total = total;
    }

    @Override
    public String toString() {
        return correct + "/" + total + " correct";
    }
}

class Attempt {
    private final Student student;
    private final Examination exam;
    private final Map<Integer, String> answers = new HashMap<>();
    private AttemptStatus status = AttemptStatus.IN_PROGRESS;
    private Result result;

    Attempt(Student student, Examination exam) {
        this.student = student;
        this.exam = exam;
    }

    void answer(int questionNumber, String answer) {
        if (status == AttemptStatus.SUBMITTED) {
            System.out.println("Cannot change answers: '" + exam.getTitle() + "' is already submitted.");
            return;
        }
        Question q = exam.getQuestion(questionNumber);
        if (q == null) {
            System.out.println("Question " + questionNumber + " does not exist.");
            return;
        }
        if (!q.isValidAnswer(answer)) {
            System.out.println("'" + answer + "' is not a valid answer for Question " + questionNumber + ".");
            return;
        }
        answers.put(questionNumber, answer);
        System.out.println("Question " + questionNumber + " answered with '" + answer + "'.");
    }

    void submit() {
        if (status == AttemptStatus.SUBMITTED) {
            System.out.println("'" + exam.getTitle() + "' was already submitted.");
            return;
        }
        status = AttemptStatus.SUBMITTED;
        System.out.println("Examination '" + exam.getTitle() + "' submitted successfully.");
        result = evaluate();
        System.out.println("Result for '" + exam.getTitle() + "' attempt: " + result);
    }

    private Result evaluate() {
        int correct = 0;
        for (Question q : exam.getQuestions()) {
            String given = answers.get(q.getNumber());
            if (given != null && q.isCorrect(given)) {
                correct++;
            }
        }
        return new Result(correct, exam.getQuestions().size());
    }

    boolean isSubmitted() {
        return status == AttemptStatus.SUBMITTED;
    }

    Examination getExam() {
        return exam;
    }
}

class Student {
    private final String name;
    private final List<Attempt> attempts = new ArrayList<>();

    Student(String name) {
        this.name = name;
    }

    Attempt startExam(Examination exam) {
        for (Attempt a : attempts) {
            if (a.getExam() == exam) {
                if (a.isSubmitted()) {
                    System.out.println(name + " has already submitted '" + exam.getTitle() + "'.");
                    return null;
                }
                return a;
            }
        }
        Attempt attempt = new Attempt(this, exam);
        attempts.add(attempt);
        System.out.println("Examination '" + exam.getTitle() + "' started by " + name + ".");
        return attempt;
    }
}

public class OnlineExamSystem {
    public static void main(String[] args) {
        Examination mathQuiz = new Examination("Math Quiz");
        mathQuiz.addQuestion(new MultipleChoiceQuestion(1, "2 + 2 = ?", new String[]{"A", "B", "C", "D"}, "A"));
        mathQuiz.addQuestion(new MultipleChoiceQuestion(2, "Square root of 81?", new String[]{"A", "B", "C", "D"}, "B"));

        Student student = new Student("Student");
        Attempt attempt = student.startExam(mathQuiz);
        attempt.answer(1, "A");
        attempt.answer(2, "C");
        attempt.submit();

        System.out.println();
        attempt.answer(2, "B");
        student.startExam(mathQuiz);

        System.out.println();
        Examination science = new Examination("Science Quiz");
        science.addQuestion(new TrueFalseQuestion(1, "Water boils at 100 C at sea level", true));
        science.addQuestion(new MultipleChoiceQuestion(2, "Closest planet to the Sun?", new String[]{"A", "B", "C"}, "C"));
        Attempt second = student.startExam(science);
        second.answer(1, "true");
        second.answer(2, "C");
        second.submit();
    }
}
