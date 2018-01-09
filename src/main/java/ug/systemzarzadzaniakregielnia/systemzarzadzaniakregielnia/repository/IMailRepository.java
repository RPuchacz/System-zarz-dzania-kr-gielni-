package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Mail;

/**
 * Created by Lukasz on 09.01.2018.
 */
public interface IMailRepository extends JpaRepository<Mail,Long> {
}
