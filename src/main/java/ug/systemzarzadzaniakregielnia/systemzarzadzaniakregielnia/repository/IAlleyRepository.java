package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Alley;

/**
 * Created by Lukasz on 09.01.2018.
 */

public interface IAlleyRepository extends JpaRepository<Alley,Long> {
}
