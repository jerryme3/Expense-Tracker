import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExpenseFileCreator {

    public static boolean createFileForAllExpenses(List<String> listOfExpenses) {
        final String EXPENSE_SUM = "C:\\Users\\ASUS\\Maven Projects\\ExpenseTrackerProject\\expense_summary.txt";

        try (FileWriter fileWriter = new FileWriter(EXPENSE_SUM)) {
            StringBuilder toBeWritten = new StringBuilder("=========================ALL EXPENSES=========================");
            toBeWritten.append(String.format("%n%-10s %-30s %-10s %-10s", "ID", "Description", "Amount", "Date"));

            for (String expense : listOfExpenses) {
                String[] split = expense.split(",,,");

                toBeWritten.append("\n").append(String.format("%-10d %-30s %-10.2f %-10s",
                        Integer.parseInt(split[0]), split[1], Double.parseDouble(split[2]), split[3]));
            }

            fileWriter.write(toBeWritten.toString());

            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public static boolean createFileForSpecificMonth(List<Expense> listOfMonthExpense, int month) {
        String dateMonth = switch (month) {
            case 1 -> "january";
            case 2 -> "february";
            case 3 -> "march";
            case 4 -> "april";
            case 5 -> "may";
            case 6 -> "june";
            case 7 -> "july";
            case 8 -> "august";
            case 9 -> "september";
            case 10 -> "october";
            case 11 -> "november";
            case 12 -> "december";
            default -> "Invalid month";
        };

        if (dateMonth.equals("Invalid month")) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }

        //Change this directory once cloned to your PC :)
        final String MONTH_DIR = "C:\\Users\\ASUS\\Maven Projects\\ExpenseTrackerProject\\months\\" + dateMonth + "_summary.txt";

        try (FileWriter fileWriter = new FileWriter(MONTH_DIR)) {
            StringBuilder toBeWritten = new StringBuilder("=========================ALL EXPENSES=========================");
            toBeWritten.append(String.format("%n%-10s %-30s %-10s %-10s", "ID", "Description", "Amount", "Date"));

            for (Expense expense : listOfMonthExpense) {
                toBeWritten.append("\n").append(String.format("%-10d %-30s %-10.2f %-10s",
                        expense.id(), expense.desc(), expense.amount(), expense.expenseDate().toString()));
            }

            fileWriter.write(toBeWritten.toString());

            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }
}
