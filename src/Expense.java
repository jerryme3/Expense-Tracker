import java.time.LocalDate;

public record Expense(int id, String desc, double amount, LocalDate expenseDate) {}
