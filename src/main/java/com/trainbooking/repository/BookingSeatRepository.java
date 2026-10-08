package com.trainbooking.repository;

import com.trainbooking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingSeatRepository
        extends JpaRepository<BookingSeat, Long> {

    List<BookingSeat> findByBookingId(Long bookingId);

    @Query("""
            SELECT COUNT(bs) > 0
            FROM BookingSeat bs
            WHERE bs.seat.id = :seatId
            AND bs.booking.schedule.id = :scheduleId
            AND bs.booking.travelDate = :travelDate
            AND bs.booking.status <> 'CANCELLED'
            """)
    boolean existsBySeatIdAndScheduleIdAndTravelDate(
            @Param("seatId") Long seatId,
            @Param("scheduleId") Long scheduleId,
            @Param("travelDate") LocalDate travelDate
    );
}
