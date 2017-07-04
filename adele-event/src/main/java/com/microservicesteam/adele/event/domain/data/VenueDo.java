package com.microservicesteam.adele.event.domain.data;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

import com.microservicesteam.adele.event.domain.Venue;

@Entity
public class VenueDo extends AbstractDo<Long> {

    public final String address;

    public final CoordinatesDo coordinates;

    @OneToMany(cascade = PERSIST)
    public final List<SectorDo> sectors;

    private VenueDo() {
        this(null, null, null, null);
    }

    private VenueDo(Long id, String address, CoordinatesDo coordinates, List<SectorDo> sectors) {
        super(id);
        this.address = address;
        this.coordinates = coordinates;
        this.sectors = sectors;
    }

    public Venue toImmutable() {
        return Venue.builder()
                .id(id)
                .address(address)
                .coordinates(coordinates.toImmutable())
                .build();
    }

    public static VenueDo fromImmutable(Venue venue) {
        return new VenueDo(venue.id(),
                venue.address(),
                CoordinatesDo.fromImmutable(venue.coordinates()),
                venue.sectors().stream()
                        .map(SectorDo::fromImmutable)
                        .collect(toList())
        );
    }

}
