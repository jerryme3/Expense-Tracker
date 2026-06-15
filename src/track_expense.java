import java.time.LocalDate;
import java.util.List;

public class track_expense {

    public static commands getCommand(String command) {
        return switch (command) {
            case "add" -> commands.add;
            case "rem" -> commands.rem;
            case "view_all" -> commands.view_all;
            case "upd" -> commands.upd;
            case "get_sum" -> commands.get_sum;
            case "get_msum" -> commands.get_msum;
            case "view_msum" -> commands.view_msum;
            default -> commands.help;
        };
    }

    public static void add(String desc, String amount) {
        if (desc.isBlank()) {
            System.err.println("Expense Description is empty.");
            return;
        }

        if (desc.length() > 25) {
            System.err.println("Expense description must be concise to lesser than 25 char.");
            return;
        }

        if (amount.length() == 0) {
            System.err.println("Expense amount is empty.");
            return;
        }

        if (!amount.matches("\\d+\\.\\d+") && !amount.matches("\\d+")) {
            System.err.println("Wrong amount formatting.");
            return;
        }

        int id;

        do {
            id = Services.generateId();
        } while (ExpenseRepository.existsById(id));

        Expense expense = new Expense(id, desc.replaceAll("\\s+", " "), Double.parseDouble(amount), LocalDate.now());

        if (ExpenseRepository.addExpense(expense)) {
            System.out.println("Expense has been added to your list!");
            return;
        }

        System.out.println("An unexpected error occurred.");

    }

    public static void update(String fieldToUpdate, String id, String updatedInfo) {
        if (id.isBlank()) {
            System.err.println("ID to update is expected but none was passed.");
            return;
        }

        if (fieldToUpdate.isBlank()) {
            System.err.println("Field to update is expected but none was passed.");
            return;
        }

        if (updatedInfo.isBlank()) {
            System.err.println("Updated information is expected but none was expected");
            return;
        }

        if (!id.matches("\\d+")) {
            System.err.println("ID should have non-numerical input.");
            return;
        }

        int intId = Integer.parseInt(id);

        if (!ExpenseRepository.existsById(intId)) {
            System.err.println("Expense ID does not exists.");
            return;
        }

        switch (fieldToUpdate) {
            case "desc" -> {
                Expense curExp = ExpenseRepository.getExpenseById(intId).orElseThrow();

                boolean updated = ExpenseRepository.updateExpense(new Expense(intId, updatedInfo, curExp.amount(), curExp.expenseDate()));

                if (updated) {
                    System.out.println("Expense description has been updated!");
                    return;
                }

                System.out.println("An unexpected error occurred.");
            }

            case "amount" -> {
                if (!updatedInfo.matches("\\d+\\.\\d+") && !updatedInfo.matches("\\d+")) {
                    System.err.println("Wrong amount format.");
                    return;
                }

                double amount = Double.parseDouble(updatedInfo);

                Expense curExp = ExpenseRepository.getExpenseById(intId).orElseThrow();

                boolean updated = ExpenseRepository.updateExpense(new Expense(intId, curExp.desc(), amount, curExp.expenseDate()));

                if (updated) {
                    System.out.println("Expense amount has been updated!");
                    return;
                }

                System.out.println("An unexpected error occurred.");

            }

            default -> System.out.println("Wrong command!");

        }
    }

    public static void remove(String id) {
        if (id.isBlank()) {
            System.err.println("Expense ID to remove is empty.");
            return;
        }

        if (!id.matches("\\d+")) {
            System.err.println("Expense ID should not contains non-numerical character/s.");
            return;
        }

        int intId = Integer.parseInt(id);

        if (!ExpenseRepository.existsById(intId)) {
            System.err.println("Expense ID does not exists.");
            return;
        }

        if (ExpenseRepository.removeExpense(intId)) {
            System.out.println("Expense has been removed to your list.");
            return;
        }

        System.err.println("An unexpected error occurred.");

    }

    public static void viewAllExpenses() {
        List<String> expenses = ExpenseRepository.getAllExpenses();

        if (expenses.isEmpty() || expenses.size() < 2) {
            System.err.println("Expense list is empty.");
            return;
        }

        System.out.println("=========================ALL EXPENSES=========================");
        System.out.println("    ID       Description              Amount Spent    Date");

        for (int i = 0; i < expenses.size(); i++) {
            String[] expense = expenses.get(i).split(",,,");

            System.out.printf("%d. %-10d %-25s %-10.2f %-10s%n", i+1,
                    Integer.parseInt(expense[0]), expense[1], Double.parseDouble(expense[2]), expense[3]);
        }

    }

    public static void viewMonthSummary(String month) {
        if (month.isBlank()) {
            System.err.println("Month to view is empty.");
            return;
        }

        if (!month.matches("\\d+")) {
            System.err.println("Month should not contains non-numerical character.");
            return;
        }

        int intMonth = Integer.parseInt(month);

        if (intMonth < 1 || intMonth > 12) {
            System.err.println("Invalid month number.");
            return;
        }

        List<Expense> expenses = ExpenseRepository.getAllExpensesForAMonth(intMonth);

        if (expenses.isEmpty() || expenses.size() < 2) {
            System.err.println("Expense list is empty.");
            return;
        }

        System.out.println("=========================ALL EXPENSES=========================");
        System.out.println("     ID        Description           Amount Spent    Date");

        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            System.out.printf("%d. %-10d %-25s %-10.2f %-10s%n", i+1, expense.id(), expense.desc(), expense.amount(), expense.expenseDate().toString());
        }

    }

    public static void getSummaryForAllExpenses() {
        boolean summarized = ExpenseFileCreator.createFileForAllExpenses(ExpenseRepository.getAllExpenses());

        if (summarized) {
            System.out.println("Summary for all of your expenses has been created!");
            return;
        }

        System.err.println("An unexpected error occurred.");
    }

    public static void getSummaryForSpecificMonth(String month) {
        if (month.isBlank()) {
            System.err.println("Month to get a summary is expected but none was passed.");
            return;
        }

        if (!month.matches("\\d+")) {
            System.err.println("Month should not contains non-numerical character.");
            return;
        }

        int intMonth = Integer.parseInt(month);

        if (intMonth < 1 || intMonth > 12) {
            System.err.println("Month should be between 1-12.");
            return;
        }

        List<Expense> expenses = ExpenseRepository.getAllExpensesForAMonth(intMonth);

        if (expenses.size() < 1) {
            System.err.println("No summary has been created. No expenses have been recorded during the month.");
            return;
        }

        boolean monthSummarized = ExpenseFileCreator.createFileForSpecificMonth(expenses, intMonth);

        if (monthSummarized) {
            System.out.println("Summary for the month has been created!");
            return;
        }

        System.err.println("An unexpected error occurred.");
    }

    public static void showHelp() {
        System.out.println("\nThis is the Jerme's Expense Tracker :)");
        System.out.println("This console application will help you track your daily expenses!");

        System.out.println("\nReminder: Always enter this: java track_expense \"command\" to run a command.");
        System.out.println("          Surround your details (not commands and numerical inputs) with \"\".");

        System.out.println("\nCommands: ");
        System.out.println("1. add - Adds your expenses to the list. Format: java track_expense add \"expense_desc\" expense_amount");
        System.out.println("2. rem - Removes an expense from the list. Format: java track_expense rem expense_id");
        System.out.println("""
                3. upd - Updates the specified expense's description or amount.
                         Desc Update Format: java track_expense upd desc task-id "New Description"
                         Expense Amount Update Format: java track_expense upd amount task-id new-amount""");
        System.out.println("4. view_all - Shows the user the list of his/her expenses. Format: java track_expense view_all");
        System.out.println("""
                5. view_msum - Shows the user the list of his/her expenses throughout the specific month.
                               Format: java track_expense month-number""");
        System.out.println("""
                6. get_sum - Creates a summary file of all of the user's expenses to the directory where the programs run.
                             Format: java track_expense get_sum
                             Tips: Copy the created file somewhere in your computer or rename it to avoid file overriding.""");
        System.out.println("""
                7. get_msum - Creates a summary file of all of the user's expenses for a specific month to the "months" directory.
                             Format: java track_expense get_msum month-id
                             January - 1, February - 2, March - 3, April - 4, May - 5, June - 6, July - 7, August - 8
                             September - 9, October - 10, November - 11, December - 12
                             Tips: Copy the created file somewhere in your computer or rename it to avoid file overriding.""");
    }

    private enum commands {
        add,
        rem,
        view_all,
        upd,
        get_sum,
        get_msum,
        view_msum,
        help
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Commands are expected but none was passed.");
            return;
        }

        String command = args[0];

        switch (getCommand(command)) {
            case add -> {
                if (args.length < 3) {
                    System.err.println("Fields are expected to be filled but none were passed.");
                    return;
                }

                add(args[1], args[2]);
            }
            case rem -> remove(args[1]);
            case view_all -> viewAllExpenses();
            case view_msum -> viewMonthSummary(args[1]);
            case upd -> {
                if (args.length < 4) {
                    System.err.println("Fields are expected to be filled but none were passed.");
                    return;
                }

                update(args[1], args[2], args[3]);
            }
            case get_sum -> getSummaryForAllExpenses();
            case get_msum -> getSummaryForSpecificMonth(args[1]);
            case help -> showHelp();
            default -> System.out.println("Wrong command.");
        }
    }
}


