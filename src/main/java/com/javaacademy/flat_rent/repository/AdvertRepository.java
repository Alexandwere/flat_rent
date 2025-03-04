package com.javaacademy.flat_rent.repository;

import com.javaacademy.flat_rent.entity.Advert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdvertRepository extends JpaRepository<Advert, Integer> {

    @Query(value = """
            select ad.id, ad.price, ad.is_active, ad.apartment_id, id.description
            from advert ad inner join apartment ap on ad.apartment_id = ap.id
            where city = :city;
            """, nativeQuery = true)
    Page<Advert> findAllByCity(String city, Pageable pageable);
}
