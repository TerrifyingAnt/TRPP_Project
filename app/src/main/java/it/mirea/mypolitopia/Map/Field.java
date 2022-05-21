package it.mirea.mypolitopia.Map;

public class Field {

    private static Cell[][] instance;

    public void setInstance(Cell[][] field) {
        this.instance = field;
    }

    public static Cell[][] getInstance() {
        return instance;
    }
}
