package com.microservicesteam.adele.event.domain.data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.microservicesteam.adele.event.domain.Sector;

@Entity
public class SectorDo extends AbstractDo<Long> {

    public final long capacity;

    public final PriceDo price;

    @ElementCollection
    public final List<Integer> positions;

    public SectorDo() {
        this(null, 0, null, null);
    }

    private SectorDo(Long id, long capacity, PriceDo price, List<Integer> positions) {
        super(id);
        this.capacity = capacity;
        this.price = price;
        this.positions = positions;
    }

    public Sector toImmutable() {
        return Sector.builder()
                .id(id)
                .capacity(capacity)
                .price(price.toImmutable())
                .build();
    }

    public static SectorDo fromImmutable(Sector sector) {
        return new SectorDo(
                sector.id(),
                sector.capacity(),
                PriceDo.fromImmutable(sector.price()),
                ImmutableList.copyOf(sector.positions())
        );
    }
}
