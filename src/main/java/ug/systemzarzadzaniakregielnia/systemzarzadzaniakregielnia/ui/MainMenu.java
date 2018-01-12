package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security.RoleAuth;

/**
 * Created by Lukasz on 2017.12.11.
 */


@Title("MainMenu")
@Theme("bowlingmanager")
@SpringView(ui = MainUI.class,name = MainMenu.NAME)
public class MainMenu extends VerticalLayout implements View{
    public static final String NAME = "menu";

    IPersonRepository personRepository;

    private RoleAuth roleAuth;
    private Navigator nav;
    private Button setReservation = new Button("Rezerwuj Tor");
    private Button customers = new Button("Klienci");
    private Button reservations = new Button("Rezerwacje");
    private Button newsletter = new Button("Newsletter");
    private Button alley = new Button("Tory");
    private Button graphic = new Button("Grafik");
    private Button asClient = new Button("Informacje");


    private HorizontalLayout adminMenu1;
    private HorizontalLayout adminMenu2;
    private HorizontalLayout employeeMenu1;
    private HorizontalLayout employeeMenu2;
    private HorizontalLayout forClientLayout;



    public MainMenu(IPersonRepository personRepository) {
        setStyleName("mainMenuButton");

        this.personRepository = personRepository;

        roleAuth = new RoleAuth(personRepository);

        adminMenu1 = new HorizontalLayout();
        adminMenu2 = new HorizontalLayout();
        employeeMenu1 = new HorizontalLayout();
        employeeMenu2 = new HorizontalLayout();
        forClientLayout = new HorizontalLayout();

        setSpacing(true);
        setMargin(true);

        switch (roleAuth.getRole()) {
            case ADMIN:
            {
                adminMenu1.addComponents(setReservation, reservations, customers);
                adminMenu2.addComponents(alley, newsletter,graphic);

                addComponents(adminMenu1, adminMenu2);
                setComponentAlignment(adminMenu1, Alignment.MIDDLE_CENTER);
                setComponentAlignment(adminMenu2, Alignment.MIDDLE_CENTER);
                break;
            }

            case EMPLOYEE:{
                adminMenu1.addComponents(setReservation, reservations, customers);
                adminMenu2.addComponents(alley, newsletter,graphic);

                addComponents(adminMenu1, adminMenu2);
                setComponentAlignment(adminMenu1, Alignment.MIDDLE_CENTER);
                setComponentAlignment(adminMenu2, Alignment.MIDDLE_CENTER);
                break;
            }

            case CLIENT:
            {
                forClientLayout.addComponent(asClient);
                addComponent(forClientLayout);
                setComponentAlignment(forClientLayout, Alignment.MIDDLE_CENTER);
                break;
            }

        }



        setReservation.setDescription("Rezerwuj Tor");
        customers.setDescription("Klienci");
        reservations.setDescription("Rezerwacje");
        newsletter.setDescription("Newsletter");
        alley.setDescription("Tory");
        graphic.setDescription("Grafik");
        asClient.setDescription("Informacje");


        navigationButton(setReservation,"setReservation");
        navigationButton(customers,"customers");
        navigationButton(reservations,"reservation");
        navigationButton(newsletter,"newsletter");
        navigationButton(alley,"alley");
        navigationButton(graphic,"graphic");
        navigationButton(asClient,"asClientUi");

    }

    public void navigationButton(Button button, final String pageName){
        button.addClickListener(event -> {
            if(nav!=null){
                nav.navigateTo(pageName);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        nav = getUI().getNavigator();
    }
}