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
    private Button setAppointment = new Button("Umów wizytę");
    private Button customers = new Button("Klienci");
    private Button appointments = new Button("Wizyty");
    private Button newsletter = new Button("Newsletter");
    private Button employees = new Button("Pracownicy");
    private Button clientData = new Button("Dane");
    private Button graphic = new Button("Grafik");

    private HorizontalLayout adminMenu1;
    private HorizontalLayout adminMenu2;
    private HorizontalLayout employeeMenu1;
    private HorizontalLayout employeeMenu2;
    private HorizontalLayout forClientLayout;



    public MainMenu(IPersonRepository personRepository) {
        this.setWidth("1200px");
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
                adminMenu1.addComponents(setAppointment, appointments, customers);
                adminMenu2.addComponents(employees, newsletter,graphic);

                addComponents(adminMenu1, adminMenu2);
                setComponentAlignment(adminMenu1, Alignment.MIDDLE_CENTER);
                setComponentAlignment(adminMenu2, Alignment.MIDDLE_CENTER);
                break;
            }

            case EMPLOYEE:{
                employeeMenu1.addComponents(setAppointment, appointments);
                employeeMenu2.addComponents(newsletter, customers);


                addComponents(employeeMenu1, employeeMenu2);
                setComponentAlignment(employeeMenu1, Alignment.MIDDLE_CENTER);
                setComponentAlignment(employeeMenu2, Alignment.MIDDLE_CENTER);
                break;
            }

            case CLIENT:
            {
                forClientLayout.addComponents(clientData);

                addComponent(forClientLayout);
                setComponentAlignment(forClientLayout, Alignment.MIDDLE_CENTER);
                break;
            }

        }



        setAppointment.setDescription("Umów wizytę");
        customers.setDescription("Klienci");
        appointments.setDescription("Wizyty");
        newsletter.setDescription("Newsletter");
        employees.setDescription("Pracownicy");
        graphic.setDescription("Grafik");


        navigationButton(setAppointment,"setAppointment");
        navigationButton(customers,"customers");
        navigationButton(appointments,"appointments");
        navigationButton(newsletter,"newsletter");
        navigationButton(employees,"employees");
        navigationButton(clientData,"settingsClient");
        navigationButton(graphic,"graphic");

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