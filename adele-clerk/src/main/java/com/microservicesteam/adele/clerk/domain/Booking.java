package com.microservicesteam.adele.clerk.domain;

import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Id;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
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
public class Booking {

    @Id
    @GeneratedValue
    Long id;

    String paymentId;

    Price sumPrice;

    PaymentStatus status;

    @Singular
    @OneToMany(cascade = PERSIST)
    List<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

}
