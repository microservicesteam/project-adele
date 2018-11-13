package com.microservicesteam.adele.ordermanager.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

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
@Entity(name = "Orders")
public class Order {

    @Id
    String orderId;

    String reservationId;

    OrderStatus status;

    LocalDateTime creationTimestamp;

    String name;

    String email;

    String paymentId;

    String payerId;

    LocalDateTime lastUpdated;

}
