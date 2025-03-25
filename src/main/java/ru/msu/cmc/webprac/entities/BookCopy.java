package ru.msu.cmc.webprac.entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "book_copy")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookCopy implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @ToString.Exclude
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookStatus status = BookStatus.available;
}
