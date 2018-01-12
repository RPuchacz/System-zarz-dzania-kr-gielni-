package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.errors;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.MainUI;

@SpringView(ui = MainUI.class,name = AccessError.NAME)
public class AccessError extends HorizontalLayout implements View {
    public static final String NAME = "error";

    private MainUI ad;

    public AccessError(MainUI ad) {
        this.ad = ad;
        ad.header.setHeadline("Error");
        ad.header.setBackButton(true, false);

        Label label = new Label("Brak Dostepu!");
        label.setStyleName("h1");
        this.addComponent(label);


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
