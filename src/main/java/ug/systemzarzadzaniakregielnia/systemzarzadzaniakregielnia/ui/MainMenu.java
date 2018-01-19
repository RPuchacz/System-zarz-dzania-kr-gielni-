package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.context.MessageSource;
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
    private Button setReservation = new Button();
    private Button customers = new Button();
    private Button reservations = new Button();
    private Button newsletter = new Button();
    private Button alley = new Button();
    private Button asClient = new Button();


    private HorizontalLayout adminMenu1;
    private HorizontalLayout adminMenu2;
    private HorizontalLayout forClientLayout;



    public MainMenu(IPersonRepository personRepository,MessageSource messageSource) {
        setStyleName("mainMenuButton");

        this.personRepository = personRepository;

        roleAuth = new RoleAuth(personRepository);

        adminMenu1 = new HorizontalLayout();
        adminMenu2 = new HorizontalLayout();
        forClientLayout = new HorizontalLayout();


        setSpacing(true);
        setMargin(true);

        switch (roleAuth.getRole()) {
            case ADMIN:
            {
                adminMenu1.addComponents(setReservation, reservations, customers);
                adminMenu2.addComponents(alley, newsletter,asClient);

                addComponents(adminMenu1, adminMenu2);
                setComponentAlignment(adminMenu1, Alignment.MIDDLE_CENTER);
                setComponentAlignment(adminMenu2, Alignment.MIDDLE_CENTER);
                break;
            }

            case EMPLOYEE:{
                adminMenu1.addComponents(setReservation, reservations, customers);
                adminMenu2.addComponents(alley, newsletter,asClient);

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


        setReservation.setCaption(messageSource.getMessage("common.newReservation",null, UI.getCurrent().getLocale()));
        customers.setCaption(messageSource.getMessage("common.clients",null, UI.getCurrent().getLocale()));
        reservations.setCaption(messageSource.getMessage("common.reservations",null, UI.getCurrent().getLocale()));
        newsletter.setCaption(messageSource.getMessage("common.newsletter",null, UI.getCurrent().getLocale()));
        alley.setCaption(messageSource.getMessage("common.alleys",null, UI.getCurrent().getLocale()));
        asClient.setCaption(messageSource.getMessage("common.information",null, UI.getCurrent().getLocale()));

        setReservation.setDescription(messageSource.getMessage("common.newReservation",null, UI.getCurrent().getLocale()));
        customers.setDescription(messageSource.getMessage("common.clients",null, UI.getCurrent().getLocale()));
        reservations.setDescription(messageSource.getMessage("common.reservations",null, UI.getCurrent().getLocale()));
        newsletter.setDescription(messageSource.getMessage("common.newsletter",null, UI.getCurrent().getLocale()));
        alley.setDescription(messageSource.getMessage("common.alleys",null, UI.getCurrent().getLocale()));
        asClient.setDescription(messageSource.getMessage("common.information",null, UI.getCurrent().getLocale()));


        navigationButton(setReservation,"setReservation");
        navigationButton(customers,"customers");
        navigationButton(reservations,"reservation");
        navigationButton(newsletter,"newsletter");
        navigationButton(alley,"alley");
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