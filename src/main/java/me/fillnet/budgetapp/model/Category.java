package me.fillnet.budgetapp.model;

public enum Category {
    FOOD("Еда"),
    CLOTHES("Одежда"),
    FUN("Развлечения"),
    TRANSPORT("Транспорт"),
    HOBBY("Хобби");
    private String text;

    Category(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
