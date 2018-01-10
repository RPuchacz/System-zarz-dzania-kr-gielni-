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
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Address;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IAddressRepository;
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

    private  MainUI ad;
    private HorizontalSplitPanel hsplit;

    private List<Person> personList;
    private Grid<Person> personGrid;
    private HorizontalLayout buttons;
    private VerticalLayout vl;
    private Button newButton;
    private Button editButton;
    private Button deleteButton;
    private FormLayout personLayout;
    private FormLayout adressLayout;

    @Autowired
 public Customer(MainUI ad, IPersonRepository personRepository, IAddressRepository addressRepository) {
     this.ad = ad;
     ad.header.addComponent(ad.header.headlineLayout);
     ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
     ad.header.setHeadline("Klienci");

     setHeight("1474px");
     setWidth("1250px");

     ad.header.setBackButton(true,false);

     hsplit = new HorizontalSplitPanel();
     hsplit.setSizeFull();

     addComponent(hsplit);

        personList = personRepository.findAll();
        personGrid = new Grid<>();
        buttons = new HorizontalLayout();
        vl = new VerticalLayout();
        personGrid.setItems(personList);
        personGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        SingleSelectionModel<Person> singleSelectionModel = (SingleSelectionModel<Person>) personGrid.getSelectionModel();
        singleSelectionModel.setDeselectAllowed(false);




        Binder<Person> binder = new Binder<>(Person.class);

        TextField firstName = new TextField("Imie");
        TextField lastName = new TextField("Nazwisko");
        TextField login = new TextField("Login");
        PasswordField password = new PasswordField("Haslo");
        TextField phoneNumber = new TextField("Numer Telefonu");
        TextField mail = new TextField("E-mail");


        TextField country = new TextField("Kraj");
        TextField city = new TextField("Miasto");
        TextField street = new TextField("Ulica");
        TextField postalCode = new TextField("Kod Pocztowy");
        DateField dateOfBirth = new DateField("Data Urodzenia");
        CheckBox newsletter = new CheckBox("Newsletter");
        ComboBox role = new ComboBox("Uprawnienia");

        Button saveButton = new Button("Zapisz");

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

        HorizontalLayout vlform = new HorizontalLayout();
        VerticalLayout vlPerson = new VerticalLayout();
        VerticalLayout vlAddress = new VerticalLayout();

        vlPerson.addComponents(firstName,lastName,dateOfBirth,login,password,mail,phoneNumber,saveButton);
        vlAddress.addComponents(country,city,street,postalCode,newsletter,role);

        vlform.addComponents(vlPerson,vlAddress);




        newButton = new Button("New Person", event -> {
            Person p = new Person();
            Address a = new Address();
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
            });
        });

        deleteButton = new Button("Delete Person", event -> {
            personRepository.delete(personGrid.getSelectedItems());
            personGrid.setItems();
            personGrid.setItems(personRepository.findAll());
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            hsplit.removeComponent(vlform);
        });



        personGrid.addColumn(Person::getFirstName).setCaption("Imie");
        personGrid.addColumn(Person::getLastName).setCaption("Nazwisko");
        personGrid.addColumn(Person::getPhoneNumber).setCaption("Telefon");
        personGrid.addColumn(Person::getMail).setCaption("E-mail");
        personGrid.addColumn(Person::getLoyalPoints).setCaption("Punkty");


        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

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
    }
}
