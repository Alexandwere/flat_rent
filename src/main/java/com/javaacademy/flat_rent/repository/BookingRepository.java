package com.javaacademy.flat_rent.repository;

import com.javaacademy.flat_rent.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "Select * from booking where advert_id = :advertId", nativeQuery = true)
    List<Booking> findAllByAdvertIdUsingNativeSQL(@Param("advertId") Integer id);
}
