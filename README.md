## Overview
- A CLI-based application that tracks your expenses!

## Features
- Lets the user add their expenses to their list
- Lets the user remove their expenses from their list
- Lets the user update their expenses' description or amount/price from their list
- Lets the user view all of their expenses from their list
- Lets the user view their expenses from a specific month of the year from their list
- Lets the user have a txt file copy of all expenses of their expenses
- Lets the user have a txt file copy of their expenses in a specific month

## How to set up
1. Clone the repository anywhere in your PC
2. Open the ExpenseRepository.java then change this line to a preferred directory in your PC
   ```private static final String EXPENSE_REPO_DIR = "C:\\Users\\ASUS\\Maven Projects\\ExpenseTrackerProject\\expense_repository.txt";```
3. Same for the ExpenseFileCreator.java change the value assign to these variables to a preferred directory
   ```final String EXPENSE_SUM = "C:\\Users\\ASUS\\Maven Projects\\ExpenseTrackerProject\\expense_summary.txt";```
   ```final String MONTH_DIR = "C:\\Users\\ASUS\\Maven Projects\\ExpenseTrackerProject\\months\\" + dateMonth + "_summary.txt";```
4. Save the files
5. Open the terminal to the directory where the program is located (inside the src directory)
6. Compile the track_expense.java in the terminal ```javac track_expense.java```
7. Run this command ```java track_expense help``` to have a much better overview with the app

## Commands
1. add - Adds your expenses to the list. Format: java track_expense add "expense_desc" expense_amount
2. rem - Removes an expense from the list. Format: java track_expense rem expense_id
3. upd - Updates the specified expense's description or amount.
   Desc Update Format: java track_expense upd desc task-id "New Description"
   Expense Amount Update Format: java track_expense upd amount task-id new-amount
4. view_all - Shows the user the list of his/her expenses. Format: java track_expense view_all
5. view_msum - Shows the user the list of his/her expenses throughout the specific month.
   Format: java track_expense month-number
6. get_sum - Creates a summary file of all of the user's expenses to the directory where the programs run.
   Format: java track_expense get_sum
   Tips: Copy the created file somewhere in your computer or rename it to avoid file overriding.
7. get_msum - Creates a summary file of all of the user's expenses for a specific month to the "months" directory.
   Format: java track_expense get_msum month-id
   January - 1, February - 2, March - 3, April - 4, May - 5, June - 6, July - 7, August - 8
   September - 9, October - 10, November - 11, December - 12
   Tips: Copy the created file somewhere in your computer or rename it to avoid file overriding.

## Project Link
- https://roadmap.sh/projects/expense-tracker
