package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

/**
 * Created by Lukasz on 11.12.2017.
 */
@SpringUI
public class MainUI extends UI {


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setHeight("474px");
        setWidth("1250px");

        Button button = new Button("Click");


        setContent(button);


    }
}
