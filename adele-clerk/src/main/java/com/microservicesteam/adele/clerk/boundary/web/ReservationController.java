package com.microservicesteam.adele.clerk.boundary.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesteam.adele.clerk.domain.ReservationRequest;
import com.microservicesteam.adele.clerk.domain.ReservationResponse;
import com.microservicesteam.adele.clerk.domain.ReservationsService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationsService reservationsService;

    @PostMapping
    public ReservationResponse reserveTickets(@RequestBody ReservationRequest reservationRequest) {
        return reservationsService.reserveTickets(reservationRequest.tickets());
    }

}
