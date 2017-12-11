package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;

import java.util.Arrays;
import java.util.Locale;

@SpringUI
@Theme("bowlingmanager")
@Title("BowlingManager")
public class MainUI extends UI {

    @Autowired
    private SpringViewProvider viewProvider;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private IPersonRepository personRepository;

	private static final long serialVersionUID = 1L;

	public Header header;
	public static Navigator nav;

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		if (!Arrays.asList(Locale.ENGLISH, Locale.forLanguageTag("pl")).contains(getLocale())) {
			setLocale(Locale.forLanguageTag("pl"));
			getSession().setLocale(Locale.forLanguageTag("pl"));
		}


		header = new Header(messageSource,personRepository);

        VerticalLayout vl = new VerticalLayout();

        HorizontalLayout mainView = new HorizontalLayout();

		header.setBackButton(false,true);
		header.setSizeFull();

		vl.addComponents(header, mainView);
		vl.setMargin(true);
		vl.setSpacing(true);
		setContent(vl);
		vl.setComponentAlignment(mainView, Alignment.MIDDLE_CENTER);
        nav = new Navigator(this, mainView);
        nav.addProvider(viewProvider);
        nav.navigateTo("menu");

	}


}