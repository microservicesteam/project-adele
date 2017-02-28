package com.microservicesteam.adele.model.data;

import javax.persistence.Entity;

import com.microservicesteam.adele.model.Sector;

@Entity
public class SectorDo extends AbstractDo<Long> {

    public final long capacity;
    public final PriceDo price;
    public final PositionDo position;

    public SectorDo() {
        super(null);
        capacity = 0;
        price = null;
        position = null;
    }

    public SectorDo(Long id) {
        this(id, 0, null, null);
    }

    public SectorDo(long l, long capacity, PriceDo price, PositionDo position) {
        super(l);
        this.capacity = capacity;
        this.price = price;
        this.position = position;
    }

    Sector toImmutable() {
        return Sector.builder()
                .withCapacity(capacity)
                .withPrice(price.toImmutable())
                .withPosition(position.toImmutable())
                .build();
    }

    SectorDo fromImmutable(Sector sector) {
        return new SectorDo(
                0,
                sector.capacity(),
                PriceDo.fromImmutable(sector.price()),
                PositionDo.fromImmutable(sector.position()));
    }
}
