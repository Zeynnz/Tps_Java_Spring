package org.coiffet.tp1.Article;

import jakarta.persistence.*;
import org.coiffet.tp1.Opinion.Opinion;
import org.coiffet.tp1.User.User;

import java.util.List;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int article_id;

    @Column(name = "contenu")
    private String contenu;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opinion> opinions;


    public Article() {
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public long getId() {
        return article_id;
    }
}
