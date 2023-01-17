package me.fillnet.budgetapp.services;

public interface FileService {
    boolean saveToFile(String json);

    String readFromFile();
}
