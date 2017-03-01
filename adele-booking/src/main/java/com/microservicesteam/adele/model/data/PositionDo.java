package com.microservicesteam.adele.model.data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.microservicesteam.adele.model.Position;

@Entity
public class PositionDo extends AbstractDo<Long> {
    
    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = false)
    public final SectorDo sector;
    
    private PositionDo(Long id, SectorDo sector) {
        super(id);
        this.sector = sector;
    }

    public Position toImmutable() {
        return Position.builder()
                .withId(id)
                .build();
    }

    public static PositionDo fromImmutable(Position position) {
        return new PositionDo(position.id(), SectorDo.fromImmutable(position.sector()));
    }
}
