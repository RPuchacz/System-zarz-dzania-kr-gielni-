package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.setReservation;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import java.util.ArrayList;
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
    private List<Reservation> re;
    private Button showReservations;

    @Autowired
    public SetReservationUi(MainUI ad, IPersonRepository personRepository, IAlleyRepository alleyRepository, IReservationRepository reservationRepository,MessageSource messageSource) {
        roleAuth = new RoleAuth(personRepository);
        this.ad = ad;
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline(messageSource.getMessage("common.newReservation",null, UI.getCurrent().getLocale()));
        setHeight("474px");
        setWidth("1250px");
        ad.header.setBackButton(true,false);

        hsplit = new HorizontalSplitPanel();
        hl = new VerticalLayout();
        vl = new HorizontalLayout();
        personGrid = new Grid<>();
        reservationButton = new Button(messageSource.getMessage("common.reserve",null, UI.getCurrent().getLocale()));
        startTime = new DateTimeField();
        time = new NativeSelect<>();
        alleyNativeSelect = new NativeSelect<>();
        personGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        personSingleSelectionModel = (SingleSelectionModel<Person>) personGrid.getSelectionModel();
        time.setItems(1,2,3,4,5,6,7,8,9,10);
        panel = new Panel(messageSource.getMessage("common.reservationsForAlley",null, UI.getCurrent().getLocale()));
        fl = new FormLayout();
        window = new Window();
        showReservations = new Button(messageSource.getMessage("common.showReservations",null, UI.getCurrent().getLocale()));
        showReservations.setEnabled(false);



        personGrid.setItems(personRepository.findAll());
        alleyNativeSelect.setItems(alleyRepository.findAll());


        alleyNativeSelect.setCaption(messageSource.getMessage("common.chooseAlley",null, UI.getCurrent().getLocale()));
        startTime.setCaption(messageSource.getMessage("common.start",null, UI.getCurrent().getLocale()));
        time.setCaption(messageSource.getMessage("common.timeframe",null, UI.getCurrent().getLocale()));
        alleyNativeSelect.setVisibleItemCount(5);

        personGrid.addSelectionListener(event -> {
            if (vl.getComponentIndex(alleyNativeSelect) >= 0){

            } else {
                vl.addComponents(alleyNativeSelect);
            }

        });

        alleyNativeSelect.addSelectionListener(event -> {
            vl.addComponent(hl);
            if(startTime.getValue()!=null) {
                re = reservationRepository.getAllByAlley(alleyNativeSelect.getValue());
                reservations = new ArrayList<>();
                for (Reservation r : re) {
                    if (r.getStartDate().toLocalDate().getDayOfMonth() == startTime.getValue().toLocalDate().getDayOfMonth()) {
                        reservations.add(r);
                    }
                }
            }
        });

        reservationButton.addClickListener(event -> {
            reservation = new Reservation();
            reservation.setAlley(alleyNativeSelect.getValue());
            reservation.setPerson(personSingleSelectionModel.getSelectedItem().get());
            reservation.setStartDate(startTime.getValue());
            reservation.setTime(time.getValue());
            reservationRepository.save(reservation);
            try {
                Sender.sendEmail(personSingleSelectionModel.getSelectedItem().get().getMail(),messageSource.getMessage("common.thanks",null, UI.getCurrent().getLocale()),messageSource.getMessage("common.info",null, UI.getCurrent().getLocale())+ " " +
                       startTime.getValue().toLocalDate() + " " +messageSource.getMessage("common.detailedInfo",null, UI.getCurrent().getLocale())+" " + startTime.getValue().toLocalTime());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            vl.removeAllComponents();
            personSingleSelectionModel.deselectAll();
        });

        startTime.addValueChangeListener(event -> {
            showReservations.setEnabled(true);
            re = reservationRepository.getAllByAlley(alleyNativeSelect.getValue());
            reservations = new ArrayList<>();
            for (Reservation r:re){
                if(r.getStartDate().toLocalDate().getDayOfMonth() == startTime.getValue().toLocalDate().getDayOfMonth()) {
                    reservations.add(r);
                }
            }
        });

        showReservations.addClickListener(event -> {
            fl.removeAllComponents();
            if (reservations.size() == 0) {
                fl.addComponent(new Label(messageSource.getMessage("common.noReservation",null, UI.getCurrent().getLocale())));
            }
            for(Reservation r : reservations) {
                fl.addComponent(new Label(messageSource.getMessage("common.res",null, UI.getCurrent().getLocale()) + " : " + " " + r.getStartDate().toLocalDate() + " " + r.getStartDate().getHour() + ":" + r.getStartDate().getMinute() + " " + messageSource.getMessage("common.to",null, UI.getCurrent().getLocale()) + " " + r.getStartDate().plusHours(r.getTime()).getHour()+":"+r.getStartDate().getMinute()));
            }
            panel.setContent(fl);
            window.setContent(panel);
            this.getUI().addWindow(window);
            window.setDraggable(true);
            window.setResizable(true);
            window.setWidth("500px");
            window.center();
        });

        hsplit.setFirstComponent(personGrid);
        hsplit.setSecondComponent(vl);
        hl.addComponents(startTime,time,new HorizontalLayout(showReservations,reservationButton));

        hsplit.setSizeFull();
        addComponent(hsplit);
        hsplit.setLocked(true);

        personGrid.removeAllColumns();
        personGrid.setCaption(messageSource.getMessage("common.choosePerson",null, UI.getCurrent().getLocale()));
        personGrid.addColumn(Person::getFirstName).setCaption(messageSource.getMessage("common.firstName",null, UI.getCurrent().getLocale()));
        personGrid.addColumn(Person::getLastName).setCaption(messageSource.getMessage("common.lastName",null, UI.getCurrent().getLocale()));
        personGrid.addColumn(Person::getPhoneNumber).setCaption(messageSource.getMessage("common.phoneNumber",null, UI.getCurrent().getLocale()));


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        ad.header.setBackButton(true,false);
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("");
        roleAuth.Auth(Role.ADMIN, Role.EMPLOYEE);
        vl.removeAllComponents();
    }
}