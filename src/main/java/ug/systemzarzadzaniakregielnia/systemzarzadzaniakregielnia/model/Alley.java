package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model;

import javax.persistence.Entity;

/**
 * Created by Lukasz on 09.01.2018.
 */
@Entity
public class Alley extends AbstractEntity {
    private String name;
    private String maxPersons;
    private String price;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(String maxPersons) {
        this.maxPersons = maxPersons;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }
}
