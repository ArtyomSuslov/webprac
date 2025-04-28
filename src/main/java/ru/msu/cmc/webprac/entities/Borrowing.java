package ru.msu.cmc.webprac.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrowing")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Borrowing implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "reader_id", nullable = false)
    @ToString.Exclude
    private Reader reader;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "book_copy_id", nullable = false)
    @ToString.Exclude
    private BookCopy bookCopy;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "return_date")
    private LocalDate returnDate;
}
