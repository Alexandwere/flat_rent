package com.javaacademy.flat_rent.repository;

import com.javaacademy.flat_rent.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = """
        Select *
        from booking
        where advert_id = :advertId;
        """, nativeQuery = true)
    List<Booking> findAllByAdvertIdUsingNativeSQL(@Param("advertId") Integer id);

    @Query(value = """
        Select b.id, b.date_start, b.date_finish, b.client_id, b.advert_id, b.result_price
        from booking b inner join client c on b.client_id = c.id
        where email = :email;
        """, nativeQuery = true)
    Page<Booking> findAllByEmail(@Param("email") String email, Pageable pageable);
}
