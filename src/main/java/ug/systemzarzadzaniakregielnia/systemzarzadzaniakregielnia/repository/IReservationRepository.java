package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Reservation;

/**
 * Created by Lukasz on 09.01.2018.
 */
public interface IReservationRepository extends JpaRepository<Reservation,Long> {
}
