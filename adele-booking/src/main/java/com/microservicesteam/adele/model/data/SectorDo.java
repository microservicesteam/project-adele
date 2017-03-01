package com.microservicesteam.adele.model.data;

import javax.persistence.Entity;

import com.microservicesteam.adele.model.Sector;

@Entity
public class SectorDo extends AbstractDo<Long> {

    public final long capacity;

    public final PriceDo price;
    
    public SectorDo() {
        super(null);
        capacity = 0;
        price = null;
    }

    private SectorDo(long id, long capacity, PriceDo price) {
        super(id);
        this.capacity = capacity;
        this.price = price;
    }

    public Sector toImmutable() {
        return Sector.builder()
                .withCapacity(capacity)
                .withPrice(price.toImmutable())
                .build();
    }

    public static SectorDo fromImmutable(Sector sector) {
        return new SectorDo(
                sector.id(),
                sector.capacity(),
                PriceDo.fromImmutable(sector.price()));
    }
}
