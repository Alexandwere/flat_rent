package com.javaacademy.flat_rent.entity;

import com.javaacademy.flat_rent.enums.ApartmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "count_rooms", nullable = false)
    private ApartmentType apartmentType;

    @ToString.Exclude
    @OneToMany(mappedBy = "apartment", fetch = FetchType.EAGER)
    private List<Advert> adverts;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Apartment apartment = (Apartment) o;
        return Objects.equals(id, apartment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
