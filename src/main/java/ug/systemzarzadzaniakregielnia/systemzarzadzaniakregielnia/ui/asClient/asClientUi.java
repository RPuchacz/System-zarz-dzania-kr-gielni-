package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.asClient;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.components.ReservationPanel;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Reservation;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IReservationRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security.RoleAuth;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.MainUI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lukasz on 12.01.2018.
 */
@SuppressWarnings("serial")
@UIScope
@SpringView(ui = MainUI.class,name = asClientUi.NAME)
public class asClientUi extends FormLayout implements View {
    public static final String NAME = "asClientUi";

    private RoleAuth roleAuth;
    private  MainUI ad;
    private Person person;
    private List<Reservation> reservationList;
    private HorizontalLayout hl;
    private HorizontalSplitPanel hsp;
    private VerticalLayout info;
    private VerticalLayout address;
    private VerticalLayout reservationLayout;
    private CheckBox newsletter;
    private Label firstName;
    private Label lastName;
    private Label login;
    private Label loyaltyPoints;
    private Label mail;
    private Label phoneNumber;
    private Label dateOfBirth;
    private Label country;
    private Label city;
    private Label street;
    private Label postalCode;
    private FormLayout fl;



    @Autowired
    public asClientUi(MainUI ad, IPersonRepository personRepository, IReservationRepository reservationRepository) {
        roleAuth = new RoleAuth(personRepository);
        this.ad = ad;
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setHeadline("Informacje");
        setWidth("1350px");

        person = personRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());

        hsp = new HorizontalSplitPanel();
        hsp.setWidth("1250px");

        info = new VerticalLayout();
        address = new VerticalLayout();
        hl = new HorizontalLayout();
        newsletter = new CheckBox("Newsletter");
        firstName = new Label( person.getFirstName());
        lastName = new Label( person.getLastName());
        login = new Label(person.getLogin());
        mail = new Label(person.getMail());
        phoneNumber = new Label(person.getPhoneNumber());
        dateOfBirth = new Label( getFormattedDate(person.getDateOfBirth()));
        loyaltyPoints = new Label("" + person.getLoyalPoints());
        country = new Label(person.getAddress().getCountry());
        city = new Label(person.getAddress().getCity());
        street = new Label(person.getAddress().getStreet());
        postalCode = new Label(person.getAddress().getPostalCode());
        fl = new FormLayout();
        reservationList = reservationRepository.getAllByPerson(person);
        reservationLayout = new VerticalLayout();
        reservationLayout.setCaption("Rezerwacje");



        fl.setSizeFull();

        info.setCaption("Informacje");
        address.setCaption("Adres : ");
        firstName.setCaption("Imie :");
        lastName.setCaption("Nazwisko : ");
        login.setCaption("Login : ");
        mail.setCaption("E-Mail : ");
        phoneNumber.setCaption("Numer Telefonu : ");
        dateOfBirth.setCaption("Data Urodzenia : ");
        loyaltyPoints.setCaption("Punkty Lojalnosciowe : ");
        country.setCaption("Panstwo : ");
        city.setCaption("Miasto : ");
        street.setCaption("Ulica : ");
        postalCode.setCaption("Kod Pocztowy : ");


        newsletter.setValue(person.getNewsletter());

        info.addComponents(firstName,login,mail,phoneNumber,dateOfBirth,loyaltyPoints);
        address.addComponents(country,city,street,postalCode,newsletter);

        hl.addComponents(info,address);


        newsletter.addValueChangeListener(event -> {
            person.setNewsletter(newsletter.getValue());
            personRepository.save(person);
        });

        fl.addComponent(hl);

        setSizeFull();
        ad.header.setBackButton(true,false);


        for(Reservation r : reservationList){
            reservationLayout.addComponent(new ReservationPanel("Rezerwacja dnia : " + r.getStartDate().toLocalDate() + " Godzina " + r.getStartDate().getHour()+":"+r.getStartDate().getMinute(),
                    r.getAlley().getPrice().toString(),r.getAlley().getMaxPersons().toString(),r.getAlley().getName(),""+r.getTime(),r.getPerson().getFirstName() + " " + r.getPerson().getLastName()));
        }

        fl.setSizeFull();
        hsp.setSizeFull();
        hsp.setFirstComponent(fl);
        hsp.setSecondComponent(reservationLayout);
        addComponent(hsp);




    }

    String getFormattedDate(Date date) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(date);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        ad.header.setBackButton(true,false);
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Informacje");
        roleAuth.Auth(Role.ADMIN, Role.EMPLOYEE, Role.CLIENT);
    }
}
