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
import java.util.List;

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
    private NativeSelect<Integer> time;
    private Button reservationButton;
    private Reservation reservation;
    private Panel panel;
    private FormLayout fl;
    private Window window;
    private List<Reservation> reservations;

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
        personGrid = new Grid<>();
        reservationButton = new Button("Rezerwuj");
        startTime = new DateTimeField();
        time = new NativeSelect<>();
        alleyNativeSelect = new NativeSelect<>();
        personGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        personSingleSelectionModel = (SingleSelectionModel<Person>) personGrid.getSelectionModel();
        time.setItems(1,2,3,4,5,6,7,8,9,10);
        panel = new Panel("Rezerwacje dla wybranego Toru");
        fl = new FormLayout();
        window = new Window();
        reservations = reservationRepository.getAllByAlley(alleyNativeSelect.getValue());

        personGrid.setItems(personRepository.findAll());
        alleyNativeSelect.setItems(alleyRepository.findAll());


        alleyNativeSelect.setCaption("Wybierz Tor");
        startTime.setCaption("Poczatek Rezerwacji");
        time.setCaption("Czas trwania");
        alleyNativeSelect.setVisibleItemCount(5);

        personGrid.addSelectionListener(event -> {
            if (vl.getComponentIndex(alleyNativeSelect) >= 0){

            } else {
                vl.addComponents(alleyNativeSelect);
            }

        });

        alleyNativeSelect.addSelectionListener(event -> {
            vl.addComponent(hl);
        });

        reservationButton.addClickListener(event -> {
            reservation = new Reservation();
            reservation.setAlley(alleyNativeSelect.getValue());
            reservation.setPerson(personSingleSelectionModel.getSelectedItem().get());
            reservation.setStartDate(startTime.getValue());
            reservation.setTime(time.getValue());
            reservationRepository.save(reservation);
            try {
                Sender.sendEmail(personSingleSelectionModel.getSelectedItem().get().getMail(),"Dziekujemy za Rezerwacje!","Dziekujemy za rezerwacje toru w dniu " +
                       startTime.getValue().toLocalDate() + " w godzinach od " + startTime.getValue().toLocalTime());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            vl.removeAllComponents();
            personSingleSelectionModel.deselectAll();
        });

        startTime.addValueChangeListener(event -> {
            panel.setContent(fl);
            window.setContent(panel);
            fl.removeAllComponents();
            for(Reservation r : reservations) {
                fl.addComponent(new Label("Rezerwacja" + " " + r.getStartDate().toString() + " do " + r.getStartDate().plusHours(r.getTime())));
            }
            this.getUI().addWindow(window);
            window.center();
        });

        hsplit.setFirstComponent(personGrid);
        hsplit.setSecondComponent(vl);
        hl.addComponents(startTime,time,reservationButton);

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
        vl.removeAllComponents();
    }
}