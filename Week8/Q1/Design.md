# Online Examination System

## Classes

| Class | Attributes | Operations |
|---|---|---|
| Student | name, attempts | startExam(exam) |
| Examination | title, questions | addQuestion(q), getQuestion(no), getQuestions() |
| Question (abstract) | number, text | isValidAnswer(ans), isCorrect(ans) |
| MultipleChoiceQuestion | options, correctOption | overrides both checks |
| TrueFalseQuestion | correctValue | overrides both checks |
| Attempt | student, exam, answers, status, result | answer(no, ans), submit(), evaluate() |
| Result | correct, total | toString() |
| AttemptStatus (enum) | IN_PROGRESS, SUBMITTED | |

## Relationships and multiplicity

- Examination 1 ◆── 1..* Question (composition, the questions belong to the exam)
- Student 1 ── 0..* Attempt
- Examination 1 ── 0..* Attempt
- Student and Examination meet through Attempt: at most 1 submitted attempt per student per exam
- Attempt 1 ── 0..1 Result (only exists after submission)
- MultipleChoiceQuestion, TrueFalseQuestion ──▷ Question (generalization)

## Object interaction

1. `Student.startExam(exam)` checks for an existing attempt. If one is already submitted it refuses, otherwise it creates a new `Attempt` in `IN_PROGRESS`.
2. `Attempt.answer(no, ans)` looks up the question in the `Examination`, asks the question whether the answer is valid, and stores it.
3. `Attempt.submit()` moves the state to `SUBMITTED`. From then on `answer()` refuses any change.
4. `evaluate()` loops through the exam's questions and calls `isCorrect()` on each one. The call is polymorphic, so the attempt never needs to know the question type.
5. A `Result` is created and printed.

A new question type (for example fill in the blank) only needs a new subclass of `Question`. `Attempt` and `Examination` don't change.
