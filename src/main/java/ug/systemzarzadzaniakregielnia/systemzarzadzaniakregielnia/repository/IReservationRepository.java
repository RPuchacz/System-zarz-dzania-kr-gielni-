package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.State;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Alley;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Reservation;

import java.util.List;

/**
 * Created by Lukasz on 09.01.2018.
 */
public interface IReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> getAllByState(State state);
    @Query("select r from Reservation r where r.person = ?1")
    List<Reservation> getAllByPerson(Person person);
    @Query("select r from Reservation r where r.alley = ?1")
    List<Reservation> getAllByAlley(Alley alley);
}
