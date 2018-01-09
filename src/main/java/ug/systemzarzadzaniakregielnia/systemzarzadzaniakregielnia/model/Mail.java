package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.Date;

/**
 * Created by Lukasz on 2016-10-08.
 */
@Entity
public class Mail extends AbstractEntity {
//
    private String mail;
    private String title;
    private Date date;
    @Lob
    private String content;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
