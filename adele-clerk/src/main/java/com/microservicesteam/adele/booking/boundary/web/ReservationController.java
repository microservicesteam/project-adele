package com.microservicesteam.adele.booking.boundary.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesteam.adele.booking.domain.ReservationRequest;
import com.microservicesteam.adele.booking.domain.ReservationResponse;
import com.microservicesteam.adele.booking.domain.ReservationsService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationsService reservationsService;

    @PostMapping
    public ReservationResponse reservePositions(@RequestBody ReservationRequest reservationRequest) {
        return reservationsService.reservePositions(reservationRequest.positions());
    }

}
