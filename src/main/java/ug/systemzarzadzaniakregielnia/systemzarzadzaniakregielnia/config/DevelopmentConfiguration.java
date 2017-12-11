package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Address;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;

import java.util.List;


/**
 * Created by Lukasz on 2017-12-11.
 */

@Configuration
public class DevelopmentConfiguration {
    @Autowired
    void addPerson(IPersonRepository personRepository) {

        List<Person> employeeList;

        employeeList = personRepository.findAll();

        Person person = new Person();
        person.setLogin("admin");
        person.setPassword("admin");
        person.setFirstName("user");
        person.setLastName("user");
        person.setPhoneNumber("1111111");
        person.setAddress(new Address("aaa","aaa","aaa","aaa"));
        person.setRole(Role.ADMIN);



        if(personRepository.findAll().size() == 0) {
            personRepository.save(person);
        }


    }


}
