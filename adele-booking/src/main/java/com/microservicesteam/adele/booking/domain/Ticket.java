package com.microservicesteam.adele.booking.domain;

import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class Ticket {

    @Id
    @GeneratedValue
    Long id;

    Long eventId;

    Integer position;

    Price price;

    @OneToOne
    @JoinColumn(name = "visitor_id", nullable = false)
    Visitor visitor;
    
    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "booking_id", nullable = false)
    Booking booking;

}
