import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseRepository {

    //Change this directory once cloned to your PC :)
    private static final String EXPENSE_REPO_DIR = "C:\\Users\\ASUS\\Maven Projects\\ExpenseTrackerProject\\expense_repository.txt";

    private static final String COLUMN_NAMES = "ID,,,Description,,,Amount Spent,,,Date";

    public static boolean existsById(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(EXPENSE_REPO_DIR))) {

            String currLine;

            while ((currLine = br.readLine()) != null) {
                if (currLine.contains(String.valueOf(id)))
                    return true;
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static Optional<Expense> getExpenseById(int id) {
        List<String> expenses = getAllExpenses()
                .stream()
                .filter(exp -> String.valueOf(id).equals(exp.split(",,,")[0]))
                .toList();

        String[] infoOfTheId = expenses.get(0).split(",,,");

        return Optional.of(new Expense(Integer.parseInt(infoOfTheId[0]), infoOfTheId[1], Double.parseDouble(infoOfTheId[2]), LocalDate.parse(infoOfTheId[3])));
    }

    public static boolean addExpense(Expense expense) {
        String expenseDetails = String.format("%n%d,,,%s,,,%.2f,,,%s",
                expense.id(), expense.desc(), expense.amount(), expense.expenseDate().toString());

        try (FileWriter fw = new FileWriter(EXPENSE_REPO_DIR, true)) {
            fw.append(expenseDetails);

            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public static boolean updateExpense(Expense expense) {
        List<String> expenses = getAllExpenses()
                .stream()
                .filter(exp -> !String.valueOf(expense.id()).equals(exp.split(",,,")[0]))
                .toList();

        try (FileWriter fw = new FileWriter(EXPENSE_REPO_DIR)) {
            fw.write("");

            try (FileWriter toUpdate = new FileWriter(EXPENSE_REPO_DIR, true)) {
                toUpdate.append(COLUMN_NAMES);

                for (String expens : expenses) {

                    toUpdate.append('\n').append(expens);
                }

                toUpdate.append('\n').append(String.format("%d,,,%s,,,%.2f,,,%s",
                        expense.id(), expense.desc(), expense.amount(), expense.expenseDate().toString()));
            }

            return true;

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public static boolean removeExpense(int id) {
        List<String> expenses = getAllExpenses()
                .stream()
                .filter(expense -> !String.valueOf(id).equals(expense.split(",,,")[0])
                        && !expense.equals(COLUMN_NAMES))
                .toList();

        try (FileWriter toRemove = new FileWriter(EXPENSE_REPO_DIR)) {
            toRemove.write("");

           try (FileWriter fw = new FileWriter(EXPENSE_REPO_DIR, true)) {
               fw.write(COLUMN_NAMES);
               for (String expense : expenses) {
                   fw.append("\n").append(expense);
               }
           }

           return true;

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public static List<String> getAllExpenses() {
        List<String> expenses = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EXPENSE_REPO_DIR))) {
            String expense;

            while ((expense = br.readLine()) != null) {
                if (expense.isBlank() || expense.contains(COLUMN_NAMES)) continue;

                String[] split = expense.split(",,,");
                String formatted = String.format("%d,,,%s,,,%.2f,,,%s",
                        Integer.parseInt(split[0]), split[1], Double.parseDouble(split[2]), split[3]);

                expenses.add(formatted);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return expenses;
    }

    public static List<Expense> getAllExpensesForAMonth(int month) {
        List<String> expenses = getAllExpenses()
                .stream()
                .filter(exp -> LocalDate.parse(exp.split(",,,")[3]).getMonthValue() == month)
                .toList();

        List<Expense> exp = new ArrayList<>();

        for (String expens : expenses) {
            String[] expense = expens.split(",,,");

            exp.add(new Expense(Integer.parseInt(expense[0]), expense[1], Double.parseDouble(expense[2]), LocalDate.parse(expense[3])));
        }

        return exp;
    }
}
