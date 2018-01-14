package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.setReservation;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.config.Sender;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Alley;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Reservation;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IAlleyRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IReservationRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security.RoleAuth;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.MainUI;

import javax.mail.MessagingException;

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
    private Grid<Person> personGrid;
    private NativeSelect<Alley> alleyNativeSelect;
    private VerticalLayout hl;
    private HorizontalLayout vl;
    private SingleSelectionModel<Person> personSingleSelectionModel;
    private SingleSelectionModel<Alley> alleySingleSelectionModel;
    private DateTimeField startTime;
    private DateTimeField endTime;
    private Button reservationButton;
    private Reservation reservation;

    @Autowired
    public SetReservationUi(MainUI ad, IPersonRepository personRepository, IAlleyRepository alleyRepository, IReservationRepository reservationRepository) {
        roleAuth = new RoleAuth(personRepository);
        this.ad = ad;
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Nowa Rezerwacja");
        setHeight("474px");
        setWidth("1250px");
        ad.header.setBackButton(true,false);

        hsplit = new HorizontalSplitPanel();
        hl = new VerticalLayout();
        vl = new HorizontalLayout();
        personGrid = new Grid<>(Person.class);
        reservationButton = new Button("Rezerwuj");
        startTime = new DateTimeField();
        endTime = new DateTimeField();
        alleyNativeSelect = new NativeSelect<>();
        personGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        personSingleSelectionModel = (SingleSelectionModel<Person>) personGrid.getSelectionModel();

        personGrid.setItems(personRepository.findAll());
        alleyNativeSelect.setItems(alleyRepository.findAll());


        alleyNativeSelect.setCaption("Wybierz Tor");
        startTime.setCaption("Poczatek Rezerwacji");
        endTime.setCaption("Koniec Rezerwacji");
        alleyNativeSelect.setVisibleItemCount(5);

        personGrid.addSelectionListener(event -> {
            vl.addComponents(alleyNativeSelect);
        });

        alleyNativeSelect.addSelectionListener(event -> {
            vl.addComponent(hl);
        });

        reservationButton.addClickListener(event -> {
            reservation = new Reservation();
            reservation.setAlley(alleyNativeSelect.getValue());
            reservation.setPerson(personSingleSelectionModel.getSelectedItem().get());
            reservation.setStartDate(startTime.getValue());
            reservation.setEndTime(endTime.getValue());
            reservationRepository.save(reservation);
            try {
                Sender.sendEmail(personSingleSelectionModel.getSelectedItem().get().getMail(),"Dziekujemy za Rezerwacje!","Dziekujemy za rezerwacje toru w dniu " +
                       startTime.getValue().toLocalDate() + " w godzinach od " + startTime.getValue().toLocalTime() + " do " + endTime.getValue().toLocalTime());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            vl.removeAllComponents();
            personSingleSelectionModel.deselectAll();
        });


        hsplit.setFirstComponent(personGrid);
        hsplit.setSecondComponent(vl);
        hl.addComponents(startTime,endTime,reservationButton);

        hsplit.setSizeFull();
        addComponent(hsplit);
        hsplit.setLocked(true);

        personGrid.removeAllColumns();
        personGrid.setCaption("Wybierz Osobe");
        personGrid.addColumn(Person::getFirstName).setCaption("Imie");
        personGrid.addColumn(Person::getLastName).setCaption("Nazwisko");
        personGrid.addColumn(Person::getPhoneNumber).setCaption("Telefon");


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        ad.header.setBackButton(true,false);
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Nowa Rezerwacja");
        roleAuth.Auth(Role.ADMIN, Role.EMPLOYEE);
        personSingleSelectionModel.deselectAll();
        vl.removeAllComponents();
    }
}