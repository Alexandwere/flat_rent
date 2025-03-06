package com.javaacademy.flat_rent.repository;

import com.javaacademy.flat_rent.entity.Advert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdvertRepository extends JpaRepository<Advert, Integer> {

    @Query(value = """
            from Advert ad
            where ad.apartment.city = :city
            """)
    Page<Advert> findAllByCity(String city, Pageable pageable);
}
