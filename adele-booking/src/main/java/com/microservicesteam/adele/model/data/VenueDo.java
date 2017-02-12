package com.microservicesteam.adele.model.data;

import javax.persistence.Entity;

import com.microservicesteam.adele.model.Venue;

@Entity
public class VenueDo extends AbstractDo<Long> {
    
    public final String address;
    
    private VenueDo() {
        super(null);
        this.address = null;
    }
    
    private VenueDo(Long id, String address) {
        super(id);
        this.address = address;
    }
    
    public VenueDo(String address) {
        this(null, address);
    }
    
    public Venue toImmutable() {
        return Venue.builder()
                .withId(id)
                .withAddress(address)
                .build();
    }
    
    public static VenueDo fromImmutable(Venue venue) {
        return new VenueDo(venue.id(), venue.address());
    }

}
