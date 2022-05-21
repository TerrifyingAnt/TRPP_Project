package it.mirea.mypolitopia.Map;

public class Resource {

    private ResourceType resource; // переменная хранит в себе тип ресурса
    private int count; // переменная хранит в себе количество ресурса

    public enum ResourceType {
        GOLD,
        TREE,
        STONE,
        NONE
    }

    /**
     * конструктор без параметров
     */
    public Resource() {
        resource = ResourceType.NONE;
        count = 0;
    }

    /**
     * конструктор с параметрами
     * @param resource
     * @param count
     */
    public Resource(ResourceType resource, int count) {
        this.resource = resource;
        this.count = count;
    }

    //ГЕТТЕРЫ
    // возвращает ресурс, лежащий в клетке
    public ResourceType getResource() {
        return resource;
    }

    // возвращает количество игрового ресурса
    public int getCount() {
        return count;
    }

    //СЕТТЕРЫ
    // устанавливает тип игрового ресурса на клетке
    public void setResource(ResourceType resource) {
        this.resource = resource;
    }

    // устанавливает количество игрового ресурса на клетке
    public void setCount(int count) {
        this.count = count;
    }
}
