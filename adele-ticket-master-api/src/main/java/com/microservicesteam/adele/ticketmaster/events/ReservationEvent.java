package com.microservicesteam.adele.ticketmaster.events;

import com.microservicesteam.adele.ticketmaster.model.Reservation;

public interface ReservationEvent extends Event {

    Reservation reservation();
}
