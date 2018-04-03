package com.microservicesteam.adele.programmanager.domain;

import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
@Entity
public class Program {

    @Id
    @GeneratedValue
    Long id;

    String name;

    String description;

    ProgramStatus status;

    LocalDateTime dateTime;

    @OneToOne(cascade = PERSIST)
    Venue venue;

}
