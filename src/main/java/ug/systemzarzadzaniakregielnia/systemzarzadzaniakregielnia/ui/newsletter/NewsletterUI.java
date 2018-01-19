package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.newsletter;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.config.Sender;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Mail;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IMailRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security.RoleAuth;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.MainUI;

import javax.mail.MessagingException;
import java.util.Date;


@SpringView(ui = UI.class,name = NewsletterUI.NAME)
public class NewsletterUI extends HorizontalLayout implements View {
    public static final String NAME = "newsletter";

    private MainUI ad;
    private RoleAuth roleAuth;

    private final TextField titleField;

    private final Label contentLbl;
    private final RichTextArea textEditor = new RichTextArea();
    private TextArea textHtml = new TextArea();

    private HorizontalLayout editorBtns = new HorizontalLayout();
    private final Button textEditorBtn;
    private final Button textHtmlBtn;

    private final Button sendBtn;

    private VerticalLayout lay = new VerticalLayout();
    private HorizontalLayout buttons = new HorizontalLayout();


    public NewsletterUI(MainUI ad, IPersonRepository personRepository, IMailRepository mailRepository, MessageSource messageSource){
        this.ad = ad;


        roleAuth = new RoleAuth(personRepository);

        ad.header.setBackButton(true, false);
        ad.header.addComponent(ad.header.headlineLayout);
        ad.header.setComponentAlignment(ad.header.headlineLayout, Alignment.TOP_CENTER);
        ad.header.setHeadline(messageSource.getMessage("common.newsletter",null, UI.getCurrent().getLocale()));

        setStyleName("newLayout");

        titleField = new TextField(messageSource.getMessage("common.title",null, UI.getCurrent().getLocale()));
        contentLbl = new Label(messageSource.getMessage("common.content",null, UI.getCurrent().getLocale()));
        textEditorBtn = new Button("Text Editor");
        textHtmlBtn = new Button("Text Html");
        sendBtn = new Button(messageSource.getMessage("common.send",null, UI.getCurrent().getLocale()));

        textEditor.setHeight(400, Unit.PIXELS);
        textEditor.setWidth(1000, Unit.PIXELS);

        textHtml.setHeight(400, Unit.PIXELS);
        textHtml.setWidth(1000, Unit.PIXELS);
        textHtml.setVisible(false);

        addComponent(lay);

        textEditorBtn.setEnabled(false);
        editorBtns.addComponents(textEditorBtn, textHtmlBtn);

        buttons.addComponents(sendBtn);
        lay.addComponents(titleField, contentLbl, editorBtns, textEditor, textHtml, buttons);
        lay.setSpacing(true);
        lay.setStyleName("editor");



        sendBtn.addClickListener(event -> {
            sendEmails(personRepository, mailRepository);
            Notification.show(messageSource.getMessage("common.sended",null, UI.getCurrent().getLocale()));
        });


        textHtmlBtn.addClickListener(event -> htmlTextClick());
        textEditorBtn.addClickListener(event -> textEditorClick());
    }

    private void textEditorClick(){
        textHtml.setVisible(false);
        textEditor.setVisible(true);
        textEditor.setValue(textHtml.getValue());

        textEditorBtn.setEnabled(false);
        textHtmlBtn.setEnabled(true);
    }


    private void htmlTextClick(){
        textHtml.setVisible(true);
        textEditor.setVisible(false);
        textHtml.setValue(textEditor.getValue());

        textHtmlBtn.setEnabled(false);
        textEditorBtn.setEnabled(true);

    }

    @Async
    private void sendEmails(IPersonRepository personRepository, IMailRepository mailRepository) {
        String title = titleField.getValue();
        String textMessage;
        if (textEditor.isVisible()) {
            textMessage = textEditor.getValue();
        } else {
            textMessage = textHtml.getValue();
        }

        Mail mail = new Mail();
        mail.setTitle(title);
        mail.setContent(textMessage);
        mail.setDate(new Date());
        mailRepository.save(mail);

            for (Person p:personRepository.findByNewsletterTrue()){
                try {
                    Sender.sendEmail(p.getMail(), title, textMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        roleAuth.Auth(Role.ADMIN, Role.EMPLOYEE);
    }
}
