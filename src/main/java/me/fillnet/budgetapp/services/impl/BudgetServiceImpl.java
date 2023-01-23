package me.fillnet.budgetapp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.fillnet.budgetapp.model.Category;
import me.fillnet.budgetapp.model.Transaction;
import me.fillnet.budgetapp.services.BudgetService;
import me.fillnet.budgetapp.services.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
public class BudgetServiceImpl implements BudgetService {
    final private FileService fileService;
    public static final int SALARY = 70000;
    public static final int SAVING = 20000;
    public static final int DAILY_BUDGET = (SALARY - SAVING / LocalDate.now().lengthOfMonth());
    public static int balance = 0;
    public static final int AVG_SALARY = SALARY;
    //    public static final int AVG_SALARY = (70000 + 70000 + 70000 + 70000 + 75000 + 75000 + 75000 + 77000 + 80000 + 80000 + 80000 + 80000) / 12;
    public static final double AVG_DAYS = 29.3;
    private static TreeMap<Month, LinkedHashMap<Long, Transaction>> transactions = new TreeMap<>();
    private static long lastId = 0;

    public BudgetServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @PostConstruct
    private void init() {
        try {
            readFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getDailyBudget() {
        return DAILY_BUDGET;
    }

    @Override
    public int getBalance() {
        return SALARY - getAllSpend() - SAVING;
    }

    @Override
    public long addTransaction(Transaction transaction) {
        LinkedHashMap<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>());
        monthTransactions.put(lastId, transaction);
        transactions.put(LocalDate.now().getMonth(), monthTransactions);
        saveToFile();
        return lastId++;
    }

    @Override
    public Transaction getTransaction(long id) {
        for (Map<Long, Transaction> trasactionsByMonth : transactions.values()) {
            Transaction transaction = trasactionsByMonth.get(id);
            if (transaction != null) {
                return transaction;
            }
        }
        return null;
    }

    @Override
    public boolean deleteTransaction(long id) {
        for (Map<Long, Transaction> trasactionsByMonth : transactions.values()) {
            if (trasactionsByMonth.containsKey(id)) {
                trasactionsByMonth.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteAllTransaction() {
        transactions = new TreeMap<>();
    }

    @Override
    public Transaction editTransaction(long id, Transaction transaction) {
        for (Map<Long, Transaction> trasactionsByMonth : transactions.values()) {
            if (trasactionsByMonth.containsKey(id)) {
                trasactionsByMonth.put(id, transaction);
                saveToFile();
                return transaction;
            }
        }

        return null;
    }

    @Override
    public int getDailyBalance() {
        return DAILY_BUDGET * LocalDate.now().getDayOfMonth() - getAllSpend();
    }

    @Override
    public int getAllSpend() {
        Map<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>());
        int sum = 0;
        for (Transaction transaction : monthTransactions.values()) {
            sum += transaction.getSum();
        }
        return sum;
    }

    @Override
    public Path createMonthlyReport(Month month) throws IOException {
        LinkedHashMap<Long, Transaction> monthlyTransaction = transactions.getOrDefault(month, new LinkedHashMap<>());
        Path path = fileService.createTempFile("monthlyReport");
        for (Transaction transaction : monthlyTransaction.values()) {
            try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.append(transaction.getCategory().getText() + ": " + transaction.getSum() + " rub. - " + transaction.getComment());
                writer.append("\n");
            }
        }
        return path;
    }

    @Override
    public void addTransactionsFromInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] array = StringUtils.split(line, '|');
                Transaction transaction = new Transaction(Category.valueOf(array[0]), Integer.valueOf(array[1]), array[2]);
                addTransaction(transaction);
            }
        }
    }

    @Override
    public int getVacationBonus(int daysCount) {
        double avgDaySalary = AVG_SALARY / AVG_DAYS;
        return (int) (daysCount * avgDaySalary);
    }

    @Override
    public int getSalaryWithVacation(int vacationDaysCount, int vacationWorkingDaysCount, int workingDaysInMonth) {
        getVacationBonus(vacationDaysCount);
        int salary = SALARY / workingDaysInMonth * (workingDaysInMonth - vacationWorkingDaysCount);
        return salary + getVacationBonus(vacationDaysCount);
    }

    private void saveToFile() {
        try {
            DataFile dataFile = new DataFile(lastId+1, transactions);
            String json = new ObjectMapper().writeValueAsString(dataFile);
            fileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        try {
            String json = fileService.readFromFile();
            DataFile dataFile = new ObjectMapper().readValue(json, new TypeReference<DataFile>() {

            });
            lastId = dataFile.getLastId();
            transactions = dataFile.getTransactions();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class DataFile{
        private long lastId;
        private TreeMap<Month, LinkedHashMap<Long, Transaction>> transactions;
    }
}
