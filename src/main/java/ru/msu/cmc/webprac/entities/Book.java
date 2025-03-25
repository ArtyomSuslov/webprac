package ru.msu.cmc.webprac.entities;

import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Book implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "year")
    private Long year;

    @Column(name = "isbn", length = 13, nullable = false, unique = true)
    private String isbn;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<BookCopy> bookCopyList;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<BookAuthor> bookAuthorList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
