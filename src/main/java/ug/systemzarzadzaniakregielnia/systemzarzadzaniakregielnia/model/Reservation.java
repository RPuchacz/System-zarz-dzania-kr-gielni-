package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * Created by Lukasz on 09.01.2018.
 */
@Entity
public class Reservation extends AbstractEntity {
    @ManyToOne(optional = true, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Alley alley;
    @ManyToOne(optional = true, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Person person;
    private LocalDateTime startDate;
    private LocalDateTime endTime;


    public Alley getAlley() {
        return alley;
    }

    public void setAlley(Alley alley) {
        this.alley = alley;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
