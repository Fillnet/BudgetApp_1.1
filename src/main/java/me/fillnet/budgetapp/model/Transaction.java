package me.fillnet.budgetapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private Category category;
    private int sum;
    private String comment;

}
