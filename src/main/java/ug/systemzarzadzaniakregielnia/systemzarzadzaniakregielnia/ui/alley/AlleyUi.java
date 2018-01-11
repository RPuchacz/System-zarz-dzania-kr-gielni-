package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.alley;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Alley;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IAlleyRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security.RoleAuth;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.MainUI;

/**
 * Created by Lukasz on 09.01.2018.
 */
@SuppressWarnings("serial")
@UIScope
@SpringView(ui = MainUI.class,name = AlleyUi.NAME)
public class AlleyUi extends FormLayout implements View {
    public static final String NAME = "alley";

    private RoleAuth roleAuth;

    private  MainUI ad;
    private HorizontalSplitPanel hsplit;
    private Binder<Alley> binder;
    private final TextField price;
    private final TextField name;
    private final TextField maxNumberOfPeople;
    private Button newButton;
    private Button editButton;
    private Button deleteButton;
    private Button saveButton;
    private SingleSelectionModel<Alley> singleSelectionModel;
    private Grid<Alley> alleyGrid;
    private VerticalLayout vl;
    private HorizontalLayout buttons;
    private VerticalLayout vlform;
    private Alley a;


    @Autowired
    public AlleyUi(MainUI ad, IPersonRepository personRepository, IAlleyRepository alleyRepository) {

        this.ad = ad;
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Tory");
        ad.header.setBackButton(true,false);

        setHeight("874px");
        setWidth("1250px");

        roleAuth = new RoleAuth(personRepository);
        hsplit = new HorizontalSplitPanel();
        binder = new Binder<>(Alley.class);
        name = new TextField("Nazwa");
        price = new TextField("Cena");
        maxNumberOfPeople = new TextField("Maksymalna liczba osob");
        saveButton = new Button("Zapisz");
        alleyGrid = new Grid<>();
        alleyGrid.setItems(alleyRepository.findAll());
        alleyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        singleSelectionModel = (SingleSelectionModel<Alley>) alleyGrid.getSelectionModel();
        singleSelectionModel.setDeselectAllowed(false);
        singleSelectionModel.deselectAll();
        vl = new VerticalLayout();
        buttons = new HorizontalLayout();
        vlform = new VerticalLayout();

        binder.forField(name).bind(Alley::getName,Alley::setName);
        binder.forField(price).bind(Alley::getPrice,Alley::setPrice);
        binder.forField(maxNumberOfPeople).bind(Alley::getMaxPersons,Alley::setMaxPersons);

        vlform.addComponents(name,price,maxNumberOfPeople,saveButton);


        newButton = new Button("New Alley", event -> {
            a = new Alley();
            binder.setBean(a);
            hsplit.setSecondComponent(vlform);
            saveButton.addClickListener(event1 -> {
                alleyRepository.save(binder.getBean());
                alleyGrid.setItems();
                alleyGrid.setItems(alleyRepository.findAll());
                hsplit.removeComponent(vlform);
                singleSelectionModel.deselectAll();
            });
        });

        editButton = new Button("Edit Alley", event -> {
            binder.setBean(singleSelectionModel.asSingleSelect().getValue());
            hsplit.setSecondComponent(vlform);
            saveButton.addClickListener(event1 -> {
                alleyRepository.save(binder.getBean());
                alleyGrid.setItems();
                alleyGrid.setItems(alleyRepository.findAll());
                hsplit.removeComponent(vlform);
                singleSelectionModel.deselectAll();
            });
        });

        deleteButton = new Button("Delete Alley", event -> {
            alleyRepository.delete(alleyGrid.getSelectedItems());
            alleyGrid.setItems();
            alleyGrid.setItems(alleyRepository.findAll());
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            hsplit.removeComponent(vlform);
            singleSelectionModel.deselectAll();
        });


        alleyGrid.addColumn(Alley::getName).setCaption("Nazwa");
        alleyGrid.addColumn(Alley::getPrice).setCaption("Cena");
        alleyGrid.addColumn(Alley::getMaxPersons).setCaption("Liczba Osob");

        alleyGrid.addSelectionListener(event -> {
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
        });

        buttons.addComponents(newButton,editButton,deleteButton);
        vl.addComponents(buttons,alleyGrid);
        hsplit.setFirstComponent(vl);


        hsplit.setSizeFull();
        addComponent(hsplit);


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        ad.header.setBackButton(true,false);
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline("Tory");
        roleAuth.Auth(Role.ADMIN, Role.EMPLOYEE);
        hsplit.removeComponent(vlform);
        singleSelectionModel.deselectAll();
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}
