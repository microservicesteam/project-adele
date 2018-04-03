package com.microservicesteam.adele.programmanager.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

import org.hibernate.annotations.Immutable;

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
public class Sector {

    @Id
    @GeneratedValue
    Long id;

    long capacity;

    Price price;

    @ElementCollection
    @Immutable
    List<Integer> seats;
}
