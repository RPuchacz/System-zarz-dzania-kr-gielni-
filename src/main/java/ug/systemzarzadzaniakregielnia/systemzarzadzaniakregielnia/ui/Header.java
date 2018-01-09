package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;

import java.util.Locale;


/**
 * Created by Lukasz on 2017.12.11.
 */
public class Header extends VerticalLayout implements View {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    HorizontalLayout newHorizontalL = new HorizontalLayout();
    VerticalLayout hl = new VerticalLayout();
    HorizontalLayout imageLa = new HorizontalLayout();
    private Navigator nav;
    private Label nameLbl;
    private Label headline = new Label();
    private Link logout;
    public Button backButton = new Button();
    private Button pl;
    private Button us;
    IPersonRepository personRepository;
    private HorizontalLayout languages;
    public HorizontalLayout headlineLayout;
    private Label headlineLabel = new Label();
    public Person person;
    public String name;



    @Autowired
    public Header(MessageSource messageSource, IPersonRepository personRepository){
        this.personRepository = personRepository;
        newHorizontalL.setSizeFull();
        person = new Person();
        name = auth.getName();
        person = personRepository.findByLogin(name);
        nameLbl = new Label(person.getFirstName() + " " + person.getLastName());
        languages = new HorizontalLayout();
        headlineLayout = new HorizontalLayout();

        pl = new NativeButton(/*Locale.forLanguageTag("pl").getDisplayLanguage(Locale.forLanguageTag("pl"))*/);
        us = new NativeButton(/*Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH)*/);

        pl.addClickListener(event -> {
            setLanguage(Locale.forLanguageTag("pl"));
        });

        pl.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        pl.setIcon(new ThemeResource("images/pl.gif"));

        us.addClickListener(event -> {
            setLanguage(Locale.ENGLISH);
        });
        us.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        us.setIcon(new ThemeResource("images/us.gif"));

        logout = new Link(messageSource.getMessage("common.logout",null, UI.getCurrent().getLocale()),  new ExternalResource(
                "http://localhost:8080/logout") );

        backButton.setWidth(null);
        headline.setWidth(null);
        headline.setVisible(false);
        hl.setSizeUndefined();
        hl.addComponents(nameLbl,logout);
        languages.addComponents(pl,us);

        newHorizontalL.addComponents(backButton,imageLa,headline,hl);
        newHorizontalL.setComponentAlignment(backButton,Alignment.TOP_LEFT);
        newHorizontalL.setComponentAlignment(headline,Alignment.TOP_CENTER);
        addComponents(languages,newHorizontalL);
        languages.setComponentAlignment(pl,Alignment.BOTTOM_RIGHT);
        languages.setComponentAlignment(us,Alignment.BOTTOM_RIGHT);
        languages.setSizeFull();
        languages.setStyleName("languagesComponent");
        headlineLayout.addComponent(headlineLabel);


        newHorizontalL.setSpacing(true);
        newHorizontalL.setMargin(true);
        backButton.setIcon(new ThemeResource("images/backBtn.png"));
        backButton.addClickListener(event -> {
                this.setBackButton(false,true);
                removeComponent(headlineLayout);
                getUI().getNavigator().navigateTo("menu");
        });
    }

    public void setHeadline(String headline) {
        this.headlineLabel.setValue(headline);
    }

    public void setBackButton(Boolean backButton, Boolean logo) {
        this.backButton.setVisible(backButton);
        imageLa.setVisible(logo);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }


    public void setLanguage(Locale locale) {
        getUI().getSession().setLocale((Locale) locale);
        getUI().close();
        getUI().getPage().reload();
    }
}
