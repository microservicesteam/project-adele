package com.microservicesteam.adele.ordermanager.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservedTicket, Long> {

    ReservedTicket findReservationByReservationId(String reservationId);
}
