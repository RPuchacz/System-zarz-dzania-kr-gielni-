package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.reservation;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.State;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Reservation;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IReservationRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security.RoleAuth;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.MainUI;

import java.util.EnumSet;

/**
 * Created by Lukasz on 09.01.2018.
 */
@SuppressWarnings("serial")
@UIScope
@SpringView(ui = MainUI.class,name = ReservationUi.NAME)
public class ReservationUi extends FormLayout implements View {
    public static final String NAME = "reservation";

    private RoleAuth roleAuth;
    private  MainUI ad;
    private HorizontalSplitPanel hsplit;
    private Grid<Reservation> reservationGrid;
    private NativeSelect<State> stateSelect;
    private Button clean;
    private Button delete;
    private SingleSelectionModel<Reservation> singleSelectionModel;
    private Label firstName;
    private Label lastName;
    private Label mail;
    private Label phoneNumber;
    private Label price;
    private Label startDate;
    private Label time;
    private NativeSelect<State> setState;
    private Button saveButton;
    private Reservation reservation;
    private HorizontalLayout hl;
    private Button edit;

    @Autowired
    public ReservationUi(MainUI ad, IPersonRepository personRepository, IReservationRepository reservationRepository) {
        roleAuth = new RoleAuth(personRepository);
        this.ad = ad;
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Rezerwacje");

        setHeight("674px");
        setWidth("1250px");
        ad.header.setBackButton(true,false);

        reservationGrid = new Grid<>(Reservation.class);
        hsplit = new HorizontalSplitPanel();
        stateSelect = new NativeSelect<>("Stan Rezerwacji");
        clean = new Button("Usun filtr");
        singleSelectionModel = (SingleSelectionModel<Reservation>) reservationGrid.getSelectionModel();
        setState  = new NativeSelect<>();
        saveButton = new Button("Zapisz");
        delete = new Button("Anuluj Rezerwacje");
        hl = new HorizontalLayout();
        firstName = new Label();
        lastName = new Label();
        mail = new Label();
        phoneNumber = new Label();
        price = new Label();
        startDate = new Label();
        time = new Label();
        edit = new Button("Edytuj");

        price.setCaption("Cena za godzine");
        firstName.setCaption("Imie");
        lastName.setCaption("Nazwisko");
        mail.setCaption("E-Mail");
        phoneNumber.setCaption("Numer Telefonu");
        startDate.setCaption("Poczatek");
        time.setCaption("Czas trwania");

        singleSelectionModel.setDeselectAllowed(false);
        reservationGrid.setItems(reservationRepository.findAll());
        stateSelect.setItems(EnumSet.allOf(State.class));
        setState.setItems(EnumSet.allOf(State.class));
        edit.setEnabled(false);
        delete.setEnabled(false);
        setState.setCaption("Status Rezerwacji");


        hsplit.setFirstComponent(new VerticalLayout(new HorizontalLayout(stateSelect,clean,edit,delete),reservationGrid));
        hsplit.setSecondComponent(hl);
        hsplit.setSizeFull();
        addComponent(hsplit);


        stateSelect.addSelectionListener(event -> {
            reservationGrid.setItems();
            reservationGrid.setItems(reservationRepository.getAllByState(stateSelect.getValue()));
        });

        clean.addClickListener(event -> {
            stateSelect.setSelectedItem(null);
            reservationGrid.setItems();
            reservationGrid.setItems(reservationRepository.findAll());
        });

        reservationGrid.addSelectionListener(event -> {
            edit.setEnabled(true);
            delete.setEnabled(true);
        });

        delete.addClickListener(event -> {
            reservationRepository.delete(reservationGrid.getSelectedItems());
            reservationGrid.deselectAll();
            reservationGrid.setItems();
            reservationGrid.setItems(reservationRepository.getAllByState(stateSelect.getValue()));
            delete.setEnabled(false);
            edit.setEnabled(false);
            hl.removeAllComponents();
        });

        saveButton.addClickListener(event -> {
            reservation = singleSelectionModel.getSelectedItem().get();
            reservation.setState(setState.getValue());
            reservationRepository.save(reservation);
            reservationGrid.setItems();
            reservationGrid.setItems(reservationRepository.findAll());
            hl.removeAllComponents();
            edit.setEnabled(false);
            delete.setEnabled(false);
        });

        edit.addClickListener(event -> {
            hl.removeAllComponents();
            setState.setSelectedItem(singleSelectionModel.getSelectedItem().get().getState());
            firstName.setValue(singleSelectionModel.getSelectedItem().get().getPerson().getFirstName());
            lastName.setValue(singleSelectionModel.getSelectedItem().get().getPerson().getLastName());
            mail.setValue(singleSelectionModel.getSelectedItem().get().getPerson().getMail());
            phoneNumber.setValue(singleSelectionModel.getSelectedItem().get().getPerson().getPhoneNumber());
            price.setValue(singleSelectionModel.getSelectedItem().get().getAlley().getPrice());
            startDate.setValue(singleSelectionModel.getSelectedItem().get().getStartDate().toString());
            time.setValue(""+singleSelectionModel.getSelectedItem().get().getTime());
            hl.addComponents(new VerticalLayout(firstName,lastName,mail,phoneNumber),new VerticalLayout(price,startDate,time,setState,saveButton));
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        ad.header.setBackButton(true,false);
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Rezerwacje");
        roleAuth.Auth(Role.ADMIN, Role.EMPLOYEE);

    }
}