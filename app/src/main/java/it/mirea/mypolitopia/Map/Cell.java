package it.mirea.mypolitopia.Map;

public class Cell {

    private Resource resource; // переменная, в которой хранятся ресурсы на клетке
    private CellType type; // тип игровой клетки
    private City city = null;
    private boolean isCity = false;
    private Npc npc;
    private boolean isNPC = false;
    private boolean isEmpty; // пустая ли клетка, или на ней есть игрок\город
    private boolean active; // показывает, выбрана клектка или нет
    float[] color = new float[4];

    /**
     * тип игровой клетки
     */
    public enum CellType {
        GROUND,
        WATER
    }

    /**
     * конструктор
     */
    public Cell() {
        Resource resource = new Resource();
        this.resource = resource;
        this.setType(CellType.GROUND);
        isEmpty = true;
    }


    // ГЕТТЕРЫ
    // метод возвращает ресурсы с клетки
    public Resource getResource() {
        return resource;
    }

    // метод возвращает тип игровой клетки
    public CellType getType() {
        return type;
    }

    // метод возвразает информацию о том, есть ли на ней NPC или City
    public boolean getIsEmpty() {
        return isEmpty;
    }

    // метод возвращает город
    public City getCity() {
        return city;
    }

    // информирует о том, что есть город
    public boolean getBoolCity() {
        return isCity;
    }

    // метод возвращает обхект NPC
    public Npc getNpc() {
        return npc;
    }

    // метод возвращает, есть ли нпс на клетке
    public boolean isNPC() {
        return isNPC;
    }

    // метод возвращает, эту ли клетку выбирает пользователь
    public boolean getActive() {
        return active;
    }

    // метод возвращает цвет клетки
    public float[] getColor() {
        return color;
    }

    // СЕТТЕРЫ
    // метод устанавливает игровой ресурс в клетку
    public void setResource(Resource resource) {
        this.resource = resource;
        isEmpty = false;
    }

    /**
     * метод устанавливает тип игровой клетки
     * @param type
     */
    public void setType(CellType type) {
        this.type = type;
        if (this.type != CellType.GROUND) {
            isEmpty = false;
        }
        else {
            isEmpty = true;
        }
    }

    // метод устанавливает, есть ли на клетке NPC или City
    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    // метод устанавливает город
    public void setCity(City city) {
        this.city = city;
    }

    // информирует о том, что есть город
    public void setBoolCity(boolean city) {
        isCity = city;
    }

    // метод устанавливает обхект нпс на клетку
    public void setNpc(Npc npc) {
        this.npc = npc;
    }

    // логическая переменная о том, что нпс на клетке есть
    public void setNPC(boolean NPC) {
        isNPC = NPC;
    }

    // метод устанавливает, что данная клетка выбрана пользователем
    public void setActive(boolean active) {
        this.active = active;
    }

    // метод устанавлвиает цвет клетки
    public void setColor(float[] color) {
        this.color = color;
    }
}
