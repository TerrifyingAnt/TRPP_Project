package it.mirea.mypolitopia.Map;
import static java.lang.Math.abs;

import android.os.Build;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import it.mirea.mypolitopia.R;

public class Map {

    private static int map_size; // размер карты
    private int enemy_number; // количество врагов на карте
    private static Cell field[][]; // объявление игрового поля
    private Npc.Species species;

    // размеры карты
    // маленький - 10х10
    // средний - 16х16
    // большой - 32х32
    public enum MAP_SIZE {
        SMALL,
        MEDIUM,
        BIG
    }


    /**
     * конструктор класса
     * @param map_size
     * @param enemy_number
     * @param species
     */
    public Map(MAP_SIZE map_size, int enemy_number, Npc.Species species) {
        // установка размера карты
        switch (map_size) {
            case SMALL:
                this.map_size = 10;
                break;

            case MEDIUM:
                this.map_size = 16;
                break;

            case BIG:
                this.map_size = 32;
                break;
        }
        // установка количества соперников
        this.enemy_number = enemy_number;
        // создание игрового поля заполненного землей
        field = new Cell[this.map_size + 2][this.map_size + 2];
        for (int i = 0; i < this.map_size + 2; i++) {
            for (int j = 0; j < this.map_size + 2; j++) {
                field[i][j] = new Cell();
            }
        }
    }


    /**
     * метод создания рек без использования алгоритма Ла-Гранжа
     */
    public void generateRivers() {
        // обнуление игрового поля
        field = new Cell[map_size + 2][map_size + 2];
        for (int i = 0; i < map_size + 2; i++) {
            for (int j = 0; j < this.map_size + 2; j++) {
                field[i][j] = new Cell();
            }
        }

        // создание рандомных точек, через которые будет проходить река
        Random rand = new Random();
        int x[] = new int[map_size / 4];
        int y[] = new int[map_size / 4];
        int numbers[] = new int[map_size / 4];
        int height[] = new int[map_size + 2];


        for (int i = 0; i < map_size / 4; i++) {
            x[i] = rand.nextInt(map_size - 4) + 2;
            y[i] = rand.nextInt(map_size - 4) + 2;
            numbers[i] = x[i];
            for (int j = 0; j < i; j++) {
                if (x[j] == x[i]) {
                    i--;
                }
            }
        }

        // генерация
        Arrays.sort(x);
        Arrays.sort(y);
        int [] new_y = new int[map_size / 4];
        for (int i = 0; i < map_size / 4; i++) {
            new_y[y.length - 1 - i] = y[i];
        }
        y = new_y;

       for (int i = 0; i < map_size / 4 - 1; i++) {
            int y_abs = abs(y[i] - y[i + 1]) / 2 + 1;
            int x_abs = abs(x[i + 1] - x[i]) + 1;

            for (int n = 0; n < y_abs + 1; n++) {
                for (int m = 0; m < x_abs / y_abs - 1; m++) {
                    if(y[i] < field.length && x[i] + m < field.length) {
                        field[y[i]][x[i] + m].setType(Cell.CellType.WATER);
                        field[y[i] - 1][x[i]].setType(Cell.CellType.WATER);
                        if(y[i] + 1 < field.length) {
                            field[y[i] + 1][x[i]].setType(Cell.CellType.WATER);
                        }
                        field[y[i]][x[i] - 1 + m].setType(Cell.CellType.WATER);
                        if (x[i] + 1 + m < field.length) {
                            field[y[i]][x[i] + 1 + m].setType(Cell.CellType.WATER);
                        }
                    }
                }
                y[i] += 1;
                x[i] += x_abs / y_abs;
                if(y[i] < field.length && x[i] < field.length) {
                    field[y[i]][x[i]].setType(Cell.CellType.WATER);
                    if (x[i] + 1 < field.length) {
                        field[y[i]][x[i] + 1].setType(Cell.CellType.WATER);
                    }
                    if(y[i] + 1 < field.length) {
                        field[y[i] + 1][x[i]].setType(Cell.CellType.WATER);
                    }
                    field[y[i] - 1][x[i]].setType(Cell.CellType.WATER);
                    field[y[i]][x[i] - 1].setType(Cell.CellType.WATER);
                }
            }
        }

    }

    /**
     * метод генерирует горы по тому же принципу, что и реки
     */
    public void generateMountains() {

        // создание рандомных точек, через которые будет проходить река
        Random rand = new Random();
        int x[] = new int[map_size / 4];
        int y[] = new int[map_size / 4];
        int numbers[] = new int[map_size];



        for (int i = 0; i < map_size / 4; i++) {
            x[i] = rand.nextInt(map_size) + 1;
            y[i] = rand.nextInt(map_size) + 1;
            numbers[i] = x[i];
            for (int j = 0; j < i; j++) {
                if (x[j] == x[i]) {
                    i--;
                }
            }
        }

        for (int i = 0; i < x.length; i++) {
            for (int k = -1; k < 2; k++) {
                for (int l = -1; l < 2; l++) {
                    int goldChance = rand.nextInt(100) + 1;
                    if (goldChance > 75) {
                        field[y[i] - k][x[i] - l].setType(Cell.CellType.GROUND);
                        field[y[i] - k][x[i] - l].setResource(new Resource(Resource.ResourceType.GOLD, rand.nextInt(10)));
                    }
                    else {
                        field[y[i] - k][x[i] - l].setType(Cell.CellType.GROUND);
                        field[y[i] - k][x[i] - l].setResource(new Resource(Resource.ResourceType.STONE, rand.nextInt(10)));
                    }
                }
            }
        }
    }


    /**
     * метод генерирует деревья вокруг рек\озер\гор
     */
    public void generateTrees() {
        Random rand = new Random();
        for (int i = 1; i < map_size + 1; i++) {
            for (int j = 1; j < map_size + 1; j++) {
                if (((field[i][j].getType() == Cell.CellType.GROUND &&
                        field[i][j + 1].getType() == Cell.CellType.WATER) || (
                        field[i][j].getType() == Cell.CellType.GROUND &&
                                field[i + 1][j].getType() == Cell.CellType.WATER))
                                        || ((field[i][j].getType() == Cell.CellType.GROUND &&
                        field[i][j - 1].getType() == Cell.CellType.WATER) || (
                        field[i][j].getType() == Cell.CellType.GROUND &&
                                field[i - 1][j].getType() == Cell.CellType.WATER
                )) || (((field[i][j].getType() == Cell.CellType.GROUND &&
                        field[i][j + 1].getResource().getResource() == Resource.ResourceType.STONE) || (
                        field[i][j].getType() == Cell.CellType.GROUND &&
                                field[i + 1][j].getResource().getResource() == Resource.ResourceType.STONE)
                        || (field[i][j].getType() == Cell.CellType.GROUND &&
                        field[i][j - 1].getResource().getResource() == Resource.ResourceType.STONE ||
                        field[i][j].getType() == Cell.CellType.GROUND &&
                                field[i - 1][j].getResource().getResource() == Resource.ResourceType.STONE
                )) && field[i][j].getResource().getResource() == Resource.ResourceType.NONE)) {
                    field[i][j].setResource(new Resource(Resource.ResourceType.TREE,
                            rand.nextInt(10) + 1));

                }
            }
        }
        for (int i = 1; i < map_size + 1; i++) {
            for (int j = 1; j < map_size + 1; j++) {
                if (field[i][j].getResource().getResource() == Resource.ResourceType.NONE &&
                        (field[i][j].getType() == Cell.CellType.GROUND &&
                                (field[i - 1][j].getResource().getResource() == Resource.ResourceType.TREE
                                || field[i + 1][j].getResource().getResource() == Resource.ResourceType.TREE
                                || field[i][j - 1].getResource().getResource() == Resource.ResourceType.TREE
                                || field[i][j + 1].getResource().getResource() == Resource.ResourceType.TREE))) {
                    field[i][j].setResource(new Resource(Resource.ResourceType.TREE, rand.nextInt(10)));
                    j++;
                }

            }
        }
    }

    /**
     * метод генерации города
     * @param enemies
     */
    public void generateCity(int enemies) {
        Random rand = new Random();
        while (true) {
            int x = rand.nextInt(field.length - 4) + 2;
            int y = rand.nextInt(field.length - 4) + 2;
            if (field[y][x].getType() == Cell.CellType.GROUND) {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        field[y - i][x - j].setType(Cell.CellType.GROUND);
                        field[y - i][x - j].setResource(new Resource(Resource.ResourceType.NONE, 0));
                    }
                    field[y][x].setCity(new City(species, y, x, enemies));
                    field[y][x].setBoolCity(true);

                }
                break;
            }

        }

    }


    /**
     * метод генерирует врагов на карте
     * @param enemies
     */
    public void generateEnemies(int enemies) {

    }



    /**
     * метод вывода на экран игрового поля
     * @return
     */
    public String show() {
        String stringField  = "";
        for (int i = 1; i < map_size + 1; i++) {
            for (int j = 1; j < map_size + 1; j++) {
                switch (field[i][j].getType()) {
                    case WATER:
                        stringField += "~";
                        break;

                    case GROUND:
                        switch (field[i][j].getResource().getResource()) {
                            case TREE:
                                stringField += "^";
                                break;
                            case STONE:
                                stringField += "M";
                                break;
                            case GOLD:
                                stringField += "$";
                                break;

                            default:
                                if (field[i][j].getBoolCity()) {
                                    stringField += "C";
                                }
                                else if (field[i][j].isNPC()) {
                                    stringField += "N";
                                }
                                else {
                                    stringField += "#";
                                }
                                break;
                        }

                        //System.out.println(field[i][j].getResource().getResource());
                }
            }
            stringField += "\n";
        }
        return stringField;
    }


    // ГЕТТЕРЫ
    // метод возвращает игровое поле
    public Cell[][] getField() {
        return field;
    }

}
