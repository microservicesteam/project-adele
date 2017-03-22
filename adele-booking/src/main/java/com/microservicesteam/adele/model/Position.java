package com.microservicesteam.adele.model;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface Position {

    @Nullable
    Long id();

    String label();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ImmutablePosition.Builder {
    }

}
