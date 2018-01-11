package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;

import java.util.List;
//
/**
 * Created by Lukasz on 2017-12-11.
 */
public interface IPersonRepository extends JpaRepository<Person,Long> {
    Person findByLogin(String login);
    @Query("select e from Person e where e.newsletter = true")
    List<Person> findByNewsletterTrue();
}
