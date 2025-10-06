package org.coiffet.tp1;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opinion> opinions;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles;


    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return user_id;
    }


}
