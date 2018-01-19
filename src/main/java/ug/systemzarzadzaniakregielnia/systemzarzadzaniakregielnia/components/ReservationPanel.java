package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.components;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import org.springframework.context.MessageSource;

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

    public ReservationPanel(String panelCaption, String price, String maxPersons, String alley, String t, String person, MessageSource messageSource){
        fl = new FormLayout();
        time = new Label();
        alleyPricePerHour = new Label();
        alleyName = new Label();
        alleyMaxPerson = new Label();
        firstAndLastName = new Label();

        firstAndLastName.setCaption(messageSource.getMessage("common.namelastname",null, UI.getCurrent().getLocale())+" : ");
        time.setCaption(messageSource.getMessage("common.times",null, UI.getCurrent().getLocale())+" : ");
        alleyMaxPerson.setCaption(messageSource.getMessage("common.maxPersons",null, UI.getCurrent().getLocale())+" : ");
        alleyName.setCaption(messageSource.getMessage("common.alleyname",null, UI.getCurrent().getLocale())+" : ");
        alleyPricePerHour.setCaption(messageSource.getMessage("common.pricePerHour",null, UI.getCurrent().getLocale())+" : ");

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
