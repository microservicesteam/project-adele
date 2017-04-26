package com.microservicesteam.adele.event.domain.domain;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public interface Sector {

    @Nullable
	Long id();

    long capacity();

    Price price();

    List<Integer> positions();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutableSector.Builder {
    }
}
