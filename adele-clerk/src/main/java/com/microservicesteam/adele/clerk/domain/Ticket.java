package com.microservicesteam.adele.clerk.domain;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

//TODO ZM to be moved to booking-service by #51
@Value
@Getter(AccessLevel.NONE)
@FieldDefaults(level = AccessLevel.PUBLIC)
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
