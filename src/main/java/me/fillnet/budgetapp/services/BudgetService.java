package me.fillnet.budgetapp.services;

import me.fillnet.budgetapp.model.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Month;

public interface BudgetService {
    int getDailyBudget();

    int getBalance();

    long addTransaction(Transaction transaction);

    Transaction getTransaction(long id);

    boolean deleteTransaction(long id);

    void deleteAllTransaction();

    Transaction editTransaction(long id, Transaction transaction);

    int getDailyBalance();

    int getAllSpend();

    Path createMonthlyReport(Month month) throws IOException;

    void addTransactionsFromInputStream(InputStream inputStream) throws IOException;

    int getVacationBonus(int daysCount);

    int getSalaryWithVacation(int vacationDaysCount, int vacationWorkingDaysCount, int workingDaysInMonth);
}
