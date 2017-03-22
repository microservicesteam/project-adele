package com.microservicesteam.adele.model.data;

import com.microservicesteam.adele.model.Position;

import javax.persistence.Entity;

@Entity
public class PositionDo extends AbstractDo<Long> {

    public final String label;

    private PositionDo() {
        this(null, null);
    }

    private PositionDo(Long id, String label) {
        super(id);
        this.label = label;
    }

    public Position toImmutable() {
        return Position.builder()
                .withId(id)
                .withLabel(label)
                .build();
    }

    public static PositionDo fromImmutable(Position position) {
        return new PositionDo(position.id(), position.label());
    }
}
