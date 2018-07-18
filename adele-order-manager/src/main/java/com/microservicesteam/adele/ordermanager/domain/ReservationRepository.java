package com.microservicesteam.adele.ordermanager.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservedTicket, Long> {

    List<ReservedTicket> findReservationsByReservationId(String reservationId);
}
