package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Address;

/**
 * Created by Lukasz on 09.01.2018.
 */
public interface IAddressRepository extends JpaRepository<Address,Long> {
}
