package com.javaacademy.flat_rent.repository;

import com.javaacademy.flat_rent.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = """
            from Booking b
            where b.advert.id = :advertId
            """)
    List<Booking> findAllByAdvertId(Integer advertId);

    @Query(value = """
            from Booking b
            where b.client.email = :email
            """)
    Page<Booking> findAllByEmail(String email, Pageable pageable);
}
