package me.fillnet.budgetapp.services;

public interface FileRecipeService {
    boolean saveToFile(String json);

    String readFromFile();

}
