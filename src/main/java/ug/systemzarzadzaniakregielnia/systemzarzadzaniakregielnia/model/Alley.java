package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model;

/**
 * Created by Lukasz on 09.01.2018.
 */
public class Alley extends AbstractEntity {
    private String name;
    private int maxPersons;
    private double price;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(int maxPersons) {
        this.maxPersons = maxPersons;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
