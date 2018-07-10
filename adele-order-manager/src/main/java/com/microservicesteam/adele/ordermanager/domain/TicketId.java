package com.microservicesteam.adele.ordermanager.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@Getter(AccessLevel.NONE)
@FieldDefaults(level = AccessLevel.PUBLIC)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class TicketId {

    @Id
    @GeneratedValue
    Long id;

    long programId;

    int sectorId;

    int seatId;

    @ManyToOne
    @JoinColumn(name = "reservationId")
    ReservedTicket reservation;
}
