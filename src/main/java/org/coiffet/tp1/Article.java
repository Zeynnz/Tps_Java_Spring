package org.coiffet.tp1;

import jakarta.persistence.*;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long article_id;

    @Column(name = "contenu")
    private String contenu;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

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
