package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.enumeration.Role;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.model.Person;
import ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.repository.IPersonRepository;

import static ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.ui.MainUI.nav;




public class RoleAuth {
    private Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    private String login;
    private Person person;


    public RoleAuth(IPersonRepository personRepository){
        login = auth.getName();
        person = personRepository.findByLogin(login);
    }
    public void Auth(Role role){
        if(getRole() != role) {
            nav.navigateTo("error");
        }
    }

    public void Auth(Role role, Role role2){
        if(getRole() == role || getRole() == role2) {} else {
            nav.navigateTo("error");
        }
    }

    public void Auth(Role role, Role role2, Role role3){
        if(getRole() == role || getRole() == role2 || getRole() == role3) {} else {
            nav.navigateTo("error");
        }
    }

    public Role getRole(){
        return person.getRole();
    }
}

