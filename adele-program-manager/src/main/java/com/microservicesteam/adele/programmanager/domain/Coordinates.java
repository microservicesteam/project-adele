package com.microservicesteam.adele.programmanager.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
@Embeddable
public class Coordinates {
    
    @Column(nullable = false)
    double latitude;

    @Column(nullable = false)
    double longitude;
}
