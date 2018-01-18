package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.components;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

/**
 * Created by Lukasz on 18.01.2018.
 */
public class ReservationPanel extends Panel {
    private FormLayout fl;
    private Label time;
    private Label alleyPricePerHour;
    private Label alleyName;
    private Label alleyMaxPerson;
    private Label firstAndLastName;

    public ReservationPanel(String panelCaption,String price,String maxPersons,String alley, String t,String person){
        fl = new FormLayout();
        time = new Label();
        alleyPricePerHour = new Label();
        alleyName = new Label();
        alleyMaxPerson = new Label();
        firstAndLastName = new Label();

        firstAndLastName.setCaption("Imie i Nazwisko Rezerwujacego : ");
        time.setCaption("Czas rezerwacji : ");
        alleyMaxPerson.setCaption("Maksymalna liczba osob : ");
        alleyName.setCaption("Nazwa Toru : ");
        alleyPricePerHour.setCaption("Cena za godzine : ");

        firstAndLastName.setValue(person);
        time.setValue(t);
        alleyName.setValue(alley);
        alleyMaxPerson.setValue(maxPersons);
        alleyPricePerHour.setValue(price);

        this.setCaption(panelCaption);
        fl.addComponents(firstAndLastName,time,alleyName,alleyMaxPerson,alleyPricePerHour);
        setContent(fl);
    }
}
