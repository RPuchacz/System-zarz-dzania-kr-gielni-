package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.clients;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Address;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security.RoleAuth;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.MainUI;

import java.util.EnumSet;
import java.util.List;


@SuppressWarnings("serial")
@UIScope
@SpringView(ui = MainUI.class,name = Customer.NAME)
public class Customer extends FormLayout implements View {
    public static final String NAME = "customers";

    private RoleAuth roleAuth;
    private MainUI ad;
    private List<Person> personList;
    private Grid<Person> personGrid;
    private Button newButton;
    private Button editButton;
    private Button deleteButton;
    private Button saveButton;
    private HorizontalSplitPanel hsplit;
    private VerticalLayout vl;
    private HorizontalLayout buttons;
    private HorizontalLayout vlform;
    private VerticalLayout vlPerson;
    private VerticalLayout vlAddress;
    private Authentication auth;
    private String l;
    private Person person;
    private Person p;
    private Address a;
    private Binder<Person> binder;
    private SingleSelectionModel<Person> singleSelectionModel;
    private TextField firstName;
    private TextField lastName;
    private TextField login;
    private PasswordField password;
    private TextField phoneNumber;
    private TextField mail;
    private TextField country;
    private TextField city;
    private TextField street;
    private TextField postalCode;
    private DateField dateOfBirth;
    private CheckBox newsletter;
    private ComboBox role;


    @Autowired
    public Customer(MainUI ad, IPersonRepository personRepository ,MessageSource messageSource) {

     this.ad = ad;
     roleAuth = new RoleAuth(personRepository);
     ad.header.addComponent(ad.header.headlineLayout);
     ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
     ad.header.setHeadline("Klienci");

     setHeight("874px");
     setWidth("1250px");

     ad.header.setBackButton(true,false);

     hsplit = new HorizontalSplitPanel();
     hsplit.setSizeFull();

     addComponent(hsplit);

        vl = new VerticalLayout();
        buttons = new HorizontalLayout();
        vlform = new HorizontalLayout();
        vlPerson = new VerticalLayout();
        vlAddress = new VerticalLayout();
        personList = personRepository.findAll();
        personGrid = new Grid<>();
        personGrid.setItems(personList);
        personGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        auth = SecurityContextHolder.getContext().getAuthentication();
        l = auth.getName();
        person = personRepository.findByLogin(l);
        binder = new Binder<>(Person.class);
        singleSelectionModel = (SingleSelectionModel<Person>) personGrid.getSelectionModel();
        firstName = new TextField("Imie");
        lastName = new TextField("Nazwisko");
        login = new TextField("Login");
        password = new PasswordField("Haslo");
        phoneNumber = new TextField("Numer Telefonu");
        mail = new TextField("E-mail");
        country = new TextField("Kraj");
        city = new TextField("Miasto");
        street = new TextField("Ulica");
        postalCode = new TextField("Kod Pocztowy");
        dateOfBirth = new DateField("Data Urodzenia");
        newsletter = new CheckBox("Newsletter");
        role = new ComboBox("Uprawnienia");
        saveButton = new Button("Zapisz");

        singleSelectionModel.setDeselectAllowed(false);
        singleSelectionModel.deselectAll();
        role.setItems(EnumSet.allOf(Role.class));

        binder.forField(firstName).bind(Person::getFirstName,Person::setFirstName);
        binder.forField(lastName).bind(Person::getLastName,Person::setLastName);
        binder.forField(login).bind(Person::getLogin,Person::setLogin);
        binder.forField(password).bind(Person::getPassword,Person::setPassword);
        binder.forField(phoneNumber).bind(Person::getPhoneNumber,Person::setPhoneNumber);
        binder.forField(mail).bind(Person::getMail,Person::setMail);
        binder.forField(dateOfBirth).withConverter(new LocalDateToDateConverter()).bind(Person::getDateOfBirth,Person::setDateOfBirth);
        binder.forField(newsletter).bind(Person::getNewsletter,Person::setNewsletter);
        binder.bind(country,"address.country");
        binder.bind(city,"address.city");
        binder.bind(street,"address.street");
        binder.bind(postalCode,"address.postalCode");

        vlPerson.addComponents(firstName,lastName,dateOfBirth,login,password,mail,phoneNumber,saveButton);
        vlAddress.addComponents(country,city,street,postalCode,newsletter);

        if(person.getRole()== Role.ADMIN) {
            vlAddress.addComponent(role);
        }

        vlform.addComponents(vlPerson,vlAddress);

        newButton = new Button("New Person", event -> {
            p = new Person();
            a = new Address();
            role.addValueChangeListener(event1 -> {
                p.setRole((Role)role.getValue());
            });
            p.setAddress(a);
            binder.setBean(p);
            hsplit.setSecondComponent(vlform);
            saveButton.addClickListener(event1 -> {
                personRepository.save(binder.getBean());
                personGrid.setItems();
                personGrid.setItems(personRepository.findAll());
                hsplit.removeComponent(vlform);
                singleSelectionModel.deselectAll();
            });
        });

        editButton = new Button("Edit Person", event -> {
            binder.setBean(singleSelectionModel.asSingleSelect().getValue());
            hsplit.setSecondComponent(vlform);
            role.setValue(binder.getBean().getRole());
            role.addValueChangeListener(event2 -> {
                binder.getBean().setRole((Role)role.getValue());
            });
            saveButton.addClickListener(event1 -> {
                personRepository.save(binder.getBean());
                personGrid.setItems();
                personGrid.setItems(personRepository.findAll());
                hsplit.removeComponent(vlform);
                singleSelectionModel.deselectAll();
            });
        });

        deleteButton = new Button("Delete Person", event -> {
            personRepository.delete(personGrid.getSelectedItems());
            personGrid.setItems();
            personGrid.setItems(personRepository.findAll());
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            hsplit.removeComponent(vlform);
            singleSelectionModel.deselectAll();
        });


        personGrid.addColumn(Person::getFirstName).setCaption("Imie");
        personGrid.addColumn(Person::getLastName).setCaption("Nazwisko");
        personGrid.addColumn(Person::getPhoneNumber).setCaption("Telefon");
        personGrid.addColumn(Person::getMail).setCaption("E-mail");
        personGrid.addColumn(Person::getLoyalPoints).setCaption("Punkty");

        personGrid.addSelectionListener(event -> {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
        });

        buttons.addComponents(newButton,editButton,deleteButton);
        vl.addComponents(buttons,personGrid);
        hsplit.setFirstComponent(vl);
 }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        ad.header.setBackButton(true,false);
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Klienci");
        roleAuth.Auth(Role.ADMIN, Role.EMPLOYEE);
        singleSelectionModel.deselectAll();
        hsplit.removeComponent(vlform);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}
