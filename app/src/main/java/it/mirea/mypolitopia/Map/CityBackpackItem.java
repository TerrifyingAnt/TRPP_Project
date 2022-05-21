package it.mirea.mypolitopia.Map;

public class CityBackpackItem {
    private int goldAmount; // количество золота во всех городах
    private int treeAmount; // количество дерева во всех городах
    private int stoneAmount; // количество камня во всех городах

    /**
     * конструктор
     */
    public CityBackpackItem() {
        goldAmount = 0;
        treeAmount = 0;
        stoneAmount = 0;
    }

    // ГЕТТЕРЫ
    // метод возвращает количество золота
    public int getGoldAmount() {
        return goldAmount;
    }

    // метод возвращает количество дерева
    public int getTreeAmount() {
        return treeAmount;
    }

    // метод возвращает количество камня
    public int getStoneAmount() {
        return stoneAmount;
    }

    // СЕТТЕРЫ
    // метод устанавливает количество золота
    public void setGoldAmount(int goldAmount) {
        this.goldAmount = goldAmount;
    }

    // метод устанавливает количество дерева
    public void setTreeAmount(int treeAmount) {
        this.treeAmount = treeAmount;
    }

    // метод устанавливает количество камня
    public void setStoneAmount(int stoneAmount) {
        this.stoneAmount = stoneAmount;
    }
}
