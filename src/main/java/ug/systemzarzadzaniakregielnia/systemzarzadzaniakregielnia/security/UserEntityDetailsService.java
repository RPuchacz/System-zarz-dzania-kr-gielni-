package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;

/**
 * Created by Lukasz on 2017-12-11.
 */
@Component
public class UserEntityDetailsService implements UserDetailsService {
    @Autowired
    IPersonRepository personRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByLogin(username);
        if(person==null){
            throw new UsernameNotFoundException("");
        }

        return new UserEntityDetails(person);

    }
}
