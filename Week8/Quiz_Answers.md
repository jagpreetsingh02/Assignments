# Week 8 Quiz Answers

| Q | Answer | Reason |
|---|---|---|
| 1 | C | Each transaction type sits behind one common abstraction, and the engine calls it polymorphically, so new types plug in without touching the engine. |
| 2 | B, C, D | Car and Motorcycle inherit from Vehicle, and the call is resolved at runtime (dynamic dispatch) to the right override. No overloading is involved. |
| 3 | D | Available ⇄ Borrowed with guarded transitions is exactly what a state diagram shows. |
| 4 | C | Orders can't exist without their Customer and are deleted with it, which is a strong lifecycle dependency. |
| 5 | D | LineItems are created, owned and destroyed by the FoodOrder. |
| 6 | C | The processor depends on the IPaymentMethod abstraction, not on concrete classes. |
| 7 | C | It's about the order of messages between objects over time. |
| 8 | A, B, D | Salary is hidden, changed only through a validating method, so the object can never hold a negative salary. |
| 9 | D | A class implementing an interface is realization (dashed line, hollow triangle). |
| 10 | C | A course has 0 to 50 students, and a student can take any number of courses. |
