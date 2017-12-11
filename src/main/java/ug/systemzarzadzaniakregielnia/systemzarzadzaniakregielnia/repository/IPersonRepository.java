package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
//
/**
 * Created by Lukasz on 2017-12-11.
 */
public interface IPersonRepository extends JpaRepository<Person,Long> {
    Person findByLogin(String login);
}
