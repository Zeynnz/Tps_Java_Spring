package org.coiffet.tp1.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.coiffet.tp1.Article.Article;
import org.coiffet.tp1.Opinion.Opinion;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;

    @Setter
    @Getter
    @Column(name = "name")
    private String name;

    @Setter
    @Getter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opinion> opinions;

    @Setter
    @Getter
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles;

    @Getter
    @Setter
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Setter
    @Column(name = "password")
    private String password;

    public User() {}

    public long getId() {
        return user_id;
    }

    public void deleteArticleby(Article article) {
        articles.remove(article);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
