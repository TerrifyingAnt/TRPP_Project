package it.mirea.mypolitopia.Map;

public class Npc {

    private NpcType npcType; // тип нпс
    private int level; // уровень нпс
    private int hp; // количество хп у нпс
    private int xp; // количество опыта у нпс
    private int damage; // урон, наносимый нпс
    private int x; // x координата
    private int y; // y координата
    private Species species; // раса нпс
    private City city; // город, к которому привящан нпс
    private boolean action; // сделал ли нпс ход или нет

    // расы
    public enum Species {
        SNIGGERS,
        SNOWERS,
        SMALLEYERS
    }

    // тип нпс
    public enum NpcType {
        WARRIOR,
        SHIELDMAN
    }

    // действия, которые может сделать нпс
    enum Action {
        ATTACK,
        MINING,
        MOVE
    }

    /**
     * конструктор
     */
    public Npc(NpcType npcType, int level, City city, int x, int y) {
        this.npcType = npcType;
        this.level = level;
        this.city = city;
        this.x = x;
        this.y = y;
    }

    /**
     * метод устанавливает, что нпс сделал ход
     */
    public void makeAction() {
        action = true;
    };

    /**
     * метод очистки переменной, отвечающей за то, был ли сделан ход или нет
     */
    public void newAction() {
        action = false;
    }
}
