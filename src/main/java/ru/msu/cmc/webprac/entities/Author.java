package ru.msu.cmc.webprac.entities;

import lombok.*;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "author")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Author implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author author)) return false;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

