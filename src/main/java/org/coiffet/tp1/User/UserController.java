package org.coiffet.tp1.User;

import org.coiffet.tp1.Opinion.Opinion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/userbd")
public class UserController {

    @Autowired
    private UserRep userRep;

    @PostMapping(path="/add")
    public @ResponseBody String addUser(@RequestParam String name) {
        User n = new User();
        n.setName(name);
        userRep.save(n);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRep.findAll();
    }

    @GetMapping(path="/one")
    public @ResponseBody User getUserById(@RequestParam int id) {
        return userRep.getUsersById(id);
    }

}
