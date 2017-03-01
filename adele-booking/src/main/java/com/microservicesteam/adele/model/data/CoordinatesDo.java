package com.microservicesteam.adele.model.data;

import javax.persistence.Entity;

import com.microservicesteam.adele.model.Coordinates;

@Entity
public class CoordinatesDo extends AbstractDo<Long> {

    public final double latitude;

    public final double longitude;

    private CoordinatesDo() {
        super(null);
        this.latitude = 0;
        this.longitude = 0;
    }

    protected CoordinatesDo(Long id, double latitude, double longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates toImmutable() {
        return Coordinates.builder()
                .withId(id)
                .withLatitude(latitude)
                .withLongitude(longitude)
                .build();
    }

    public CoordinatesDo fromImmutable(Coordinates coordinates){
        return new CoordinatesDo(coordinates.id(), coordinates.latitude(), coordinates.longitude());
    }


}
