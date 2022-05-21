package it.mirea.mypolitopia.Map;

import java.util.ArrayList;
import java.util.List;

public class City {
    private int id; // ID гороода
    private List<Npc> npcs; // список нпс в городе
    private int level; // уровень города
    private static int moneyAmount; // количество денег во всех городах
    private Npc.Species species; // раса
    private int x; // координаты города по х
    private int y; // координаты города по у
    private static CityBackpackItem[] cityBackpackItems;
    private Cell[][] field;

    /**
     * конструктор для создания новых городов
     * @param species
     * @param level
     * @param id
     * @param x
     * @param y
     */
    public City(Npc.Species species, int level, int id, int x, int y) {
        this.species = species;
        this.level = level;
        moneyAmount = 0;
        npcs = new ArrayList<>();
        this.id = id;
        this.x = x;
        this.y = y;
        field[x][y].setEmpty(false);
        field = Field.getInstance();

    }


    /**
     * конструктор для создания первого города
     * @param species
     * @param x
     * @param y
     * @param enemy_numbers
     */
    public City(Npc.Species species, int x, int y, int enemy_numbers) {
        this.species = species;
        level = 1;
        id = 1;
        moneyAmount = 100;
        npcs = new ArrayList<>();
        npcs.add(new Npc(Npc.NpcType.WARRIOR, species, 0, this, x - 1, y));
        field = Field.getInstance();
        field[x - 1][y].setNPC(true);
        field[x - 1][y].setEmpty(false);
        this.x = x;
        this.y = y;
        cityBackpackItems = new CityBackpackItem[enemy_numbers];
    }


    /**
     * метод повышения уровня
     */
    public void levelUp() {
        if (moneyAmount > (level + 1) * 150 && getTreeAmount() > level * 100 && getStoneAmount() > level * 100) {
            moneyAmount -= (level + 1) * 150;
            setTreeAmount(getTreeAmount() - level * 100);
            setStoneAmount(getStoneAmount()- level * 100);
            this.level++;
        }
    }


    /**
     * метод, выполянемый перед ходом
     */
    public void newAction() {
        moneyAmount += level * 150;
        this.makeAction();
    }


    /**
     * метод выполнения хода
     */
    public void makeAction() {
        for (int i = 0; i < npcs.size(); i++) {
            npcs.get(i).newAction();
        }
    }


    /**
     * свободные клетки, куда можно заспавнить нпс
     * @param npcType
     */
    public void createNpc(Npc.NpcType npcType) {
        moneyAmount -= 300;
        int cells[] = new int[2];
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (field[y - i][x - j].getIsEmpty() && i != 0 && j != 0) {
                    cells[0] = i;
                    cells[1] = j;
                    break;
                }
            }
        }
        npcs.add(new Npc(npcType, species,0, this, x - cells[0], y - cells[1]));
    }

    // ГЕТТЕРЫ
    // метод возврашает позицию по Х
    public int getPosX() {
        return x;
    }

    // метод возвращает позицию по Y
    public int getY() {
        return y;
    }

    // метод возвращает расу
    public Npc.Species getSpecies() {
        return species;
    }

    // метод возвращает количестов дЕнЯк
    public int getMoneyAmount() {
        return moneyAmount;
    }

    // метод возвращает уровень города
    public int getLevel() {
        return level;
    }

    // метод возвращает списко NPC
    public List<Npc> getNpcs() {
        return npcs;
    }

    // метод возвращает id города
    public int getId() {
        return id;
    }

    // метод возвращает количество золота во всех городах
    public int getGoldAmount() {
        switch (species) {
            case SNOWERS:
                return cityBackpackItems[0].getGoldAmount();

            case SNIGGERS:
                return cityBackpackItems[1].getGoldAmount();

            case SMALLEYERS:
                return cityBackpackItems[2].getGoldAmount();
        }
        return 0;
    }

    // метод возвращает количество дерева во всех городах
    public int getTreeAmount() {
        switch (species) {
            case SNOWERS:
                return cityBackpackItems[0].getTreeAmount();

            case SNIGGERS:
                return cityBackpackItems[1].getTreeAmount();

            case SMALLEYERS:
                return cityBackpackItems[2].getTreeAmount();
        }
        return 0;
    }


    //метод возвращает количество камня во сех городах
    public int getStoneAmount() {
        switch (species) {
            case SNOWERS:
                return cityBackpackItems[0].getStoneAmount();

            case SNIGGERS:
                return cityBackpackItems[1].getStoneAmount();

            case SMALLEYERS:
                return cityBackpackItems[2].getStoneAmount();
        }
        return 0;
    }


    //СЕТТЕРЫ

    /**
     * метод устанавливает позицию по Х
     * @param x
     */
    public void setPosX(int x) {
        this.x = x;
    }


    /**
     * метод устанавливает позицию по Y
     * @param y
     */
    public void setPosY(int y) {
        this.y = y;
    }


    /**
     * метод устанавливает позицию по X и Y
     * @param x
     * @param y
     */
    public void setPos(int x, int y) { this.x = x; this.y = y; }


    /**
     * метод устанавливает ID города
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * метод устанавливает список NPC города
     * @param npcs
     */
    public void setNpcs(List<Npc> npcs) {
        this.npcs = npcs;
    }


    /**
     * метод устанавливает уровень города
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }


    /**
     * метод устанавливает изначальное количество денег
     * @param moneyAmount
     */
    public void setMoneyAmount(int moneyAmount) {
        this.moneyAmount = moneyAmount;
    }


    /**
     * метод устанавлвиает расу города
     * @param species
     */
    public void setSpecies(Npc.Species species) {
        this.species = species;
    }

    /**
     * метод устанавливает количество золота во всех городах
     * @param goldAmount
     */
    public void setGoldAmount(int goldAmount) {
        switch (species) {
            case SNOWERS:
                cityBackpackItems[0].setGoldAmount(goldAmount);
                break;

            case SNIGGERS:
                cityBackpackItems[1].setGoldAmount(goldAmount);
                break;

            case SMALLEYERS:
                cityBackpackItems[2].setGoldAmount(goldAmount);
                break;
        }
    }


    /**
     * метод устанавливает количество дерева во всех гроодах
     * @param treeAmount
     */
    public void setTreeAmount(int treeAmount) {
        switch (species) {
            case SNOWERS:
                cityBackpackItems[0].setTreeAmount(treeAmount);
                break;

            case SNIGGERS:
                cityBackpackItems[1].setTreeAmount(treeAmount);
                break;

            case SMALLEYERS:
                cityBackpackItems[2].setTreeAmount(treeAmount);
                break;
        }
    }

    /**
     * метод устанавлвиает количество камня во всех городах
     * @param stoneAmount
     */
    public void setStoneAmount(int stoneAmount) {
        switch (species) {
            case SNOWERS:
                cityBackpackItems[0].setStoneAmount(stoneAmount);
                break;

            case SNIGGERS:
                cityBackpackItems[1].setStoneAmount(stoneAmount);
                break;

            case SMALLEYERS:
                cityBackpackItems[2].setStoneAmount(stoneAmount);
                break;
        }
    }
}
