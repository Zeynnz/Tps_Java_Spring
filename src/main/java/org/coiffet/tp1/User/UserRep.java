package org.coiffet.tp1.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRep extends CrudRepository<User, Integer> {

    User findByName(String name);


}
