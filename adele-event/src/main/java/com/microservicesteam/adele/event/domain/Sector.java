package com.microservicesteam.adele.event.domain;

import javax.annotation.Nullable;
import java.util.List;

import org.immutables.value.Value;

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
