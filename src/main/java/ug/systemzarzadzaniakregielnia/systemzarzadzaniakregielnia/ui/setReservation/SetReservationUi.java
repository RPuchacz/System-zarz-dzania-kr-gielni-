package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.setReservation;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security.RoleAuth;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.MainUI;

/**
 * Created by Lukasz on 09.01.2018.
 */
@SuppressWarnings("serial")
@UIScope
@SpringView(ui = MainUI.class,name = SetReservationUi.NAME)
public class SetReservationUi extends FormLayout implements View {
    public static final String NAME = "setReservation";

    private RoleAuth roleAuth;

    private  MainUI ad;
    private HorizontalSplitPanel hsplit;

    public SetReservationUi(MainUI ad) {
        this.ad = ad;
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Nowa Rezerwacja");

        setHeight("474px");
        setWidth("1250px");
        ad.header.setBackButton(true,false);
        hsplit = new HorizontalSplitPanel();
        hsplit.setSizeFull();
        addComponent(hsplit);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        ad.header.setBackButton(true,false);
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Nowa Rezerwacja");
        roleAuth.Auth(Role.ADMIN, Role.EMPLOYEE);
    }
}