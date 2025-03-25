package ru.msu.cmc.webprac.entities;

import lombok.*;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reader")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reader implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "library_card_num", unique = true, length = 50)
    private String libraryCardNum;

    @Column(name = "address")
    private String address;

    @Column(name = "phone", length = 20)
    private String phoneNumber;

    public Reader(String fullName, String libraryCardNum, String address, String phoneNumber) {
        this.fullName = fullName;
        this.libraryCardNum = libraryCardNum;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reader reader)) return false;
        return Objects.equals(id, reader.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
