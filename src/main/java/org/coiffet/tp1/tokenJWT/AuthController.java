package org.coiffet.tp1.tokenJWT;

import org.coiffet.tp1.User.Role;
import org.coiffet.tp1.User.User;
import org.coiffet.tp1.User.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRep userRep;

    @Autowired
    private TokenGenerator tokenGenerator;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsernameDTO loginRequest) {
        try {
            User user = userRep.findByName(loginRequest.getUsername());
            if (user == null) {
                return ResponseEntity.status(404).body("Utilisateur non trouvé");
            }


            user.setRole(Role.PUBLISHER);
            userRep.save(user);
            return getResponseEntity(user);

        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsernameDTO registerRequest) {
        // Vérifie si l'utilisateur existe déjà
        if (userRep.findByName(registerRequest.getUsername()) != null) {
            return ResponseEntity.status(400).body("Utilisateur déjà existant");
        }

        // Crée un nouvel utilisateur
        User newUser = new User();
        newUser.setName(registerRequest.getUsername());
        newUser.setPassword(registerRequest.getPassword());
        newUser.setRole(Role.NONE);

        // Sauvegarde en base
        userRep.save(newUser);

        // Retourne le JWT directement pour auto-login
        return getResponseEntity(newUser);
    }

    private ResponseEntity<?> getResponseEntity(User user) {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));


        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                authorities
        );

        String token = tokenGenerator.generateJwtToken(authentication);
        List<String> roles = List.of(user.getRole().name());

        return ResponseEntity.ok(new JwtDTO(token, user.getName(), roles));
    }
}
