package com.microservicesteam.adele.model.data;

import javax.persistence.Entity;

import com.microservicesteam.adele.model.Position;

@Entity
public class PositionDo extends AbstractDo<Long> {
    private PositionDo(Long id) {
        super(id);
    }

    public Position toImmutable() {
        return Position.builder()
                .withId(id)
                .build();
    }

    public static PositionDo fromImmutable(Position position) {
        return new PositionDo(position.id());
    }
}
