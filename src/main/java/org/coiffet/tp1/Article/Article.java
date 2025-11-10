package org.coiffet.tp1.Article;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.coiffet.tp1.Opinion.Opinion;
import org.coiffet.tp1.User.User;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int article_id;

    @Setter
    @Getter
    @Column(name = "contenu")
    private String contenu;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User author;

    @Getter
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opinion> opinions;

    @Setter
    @Getter
    @Column(name = "date_publication")
    private LocalDate datePublication;

    public Article() {
    }

    public long getId() {
        return article_id;
    }

}
