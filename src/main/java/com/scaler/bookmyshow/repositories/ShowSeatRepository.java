package com.scaler.bookmyshow.repositories;

import com.scaler.bookmyshow.models.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    @Override
    List<ShowSeat> findAllById(Iterable<Long> showSeatIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShowSeat s WHERE s.id IN :ids")
    List<ShowSeat> findByIdWithLock(@Param("ids") List<Long> ids);
}
