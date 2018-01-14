package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.State;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Reservation;

import java.util.List;

/**
 * Created by Lukasz on 09.01.2018.
 */
public interface IReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> getAllByState(State state);
    List<Reservation> getAllBy(Person person);
}
