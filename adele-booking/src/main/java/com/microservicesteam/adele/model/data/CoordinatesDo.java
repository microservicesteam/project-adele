package com.microservicesteam.adele.model.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import com.microservicesteam.adele.model.Coordinates;

@Embeddable
public class CoordinatesDo {

    @Column(nullable = false)
    public final double latitude;

    @Column(nullable = false)
    public final double longitude;

    private CoordinatesDo() {
        this(0.0, 0.0);
    }

    protected CoordinatesDo(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates toImmutable() {
        return Coordinates.builder()
                .withLatitude(latitude)
                .withLongitude(longitude)
                .build();
    }

    public static CoordinatesDo fromImmutable(Coordinates coordinates){
        return new CoordinatesDo(coordinates.latitude(), coordinates.longitude());
    }


}
