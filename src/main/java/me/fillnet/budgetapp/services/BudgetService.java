package me.fillnet.budgetapp.services;

import me.fillnet.budgetapp.model.Transaction;

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

    int getVacationBonus(int daysCount);

    int getSalaryWithVacation(int vacationDaysCount, int vacationWorkingDaysCount, int workingDaysInMonth);
}
