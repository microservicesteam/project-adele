package com.microservicesteam.adele.model.data;

import com.microservicesteam.adele.model.Sector;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.PERSIST;

@Entity
public class SectorDo extends AbstractDo<Long> {

    public final long capacity;

    public final PriceDo price;

    @OneToMany(cascade = PERSIST)
    public final List<PositionDo> positions;

    public SectorDo() {
        this(null, 0, null, null);
    }

    private SectorDo(Long id, long capacity, PriceDo price, List<PositionDo> positions) {
        super(id);
        this.capacity = capacity;
        this.price = price;
        this.positions = positions;
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
                PriceDo.fromImmutable(sector.price()),
                sector.positions().stream()
                        .map(PositionDo::fromImmutable)
                        .collect(toList())
        );
    }
}
