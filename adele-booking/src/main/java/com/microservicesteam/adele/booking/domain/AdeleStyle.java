package com.microservicesteam.adele.booking.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.BuilderVisibility;
import org.immutables.value.Value.Style.ImplementationVisibility;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target({PACKAGE, TYPE})
@Retention(CLASS)
@JsonSerialize
@JsonDeserialize
@Value.Style(
    get = {"*"},
    init = "with*",
    typeAbstract = {"Abstract*"},
    typeImmutable = "Immutable*",
    builder = "builder",
    build = "build",    
    visibility = ImplementationVisibility.PUBLIC,
    overshadowImplementation = true,
    builderVisibility = BuilderVisibility.PUBLIC)
public @interface AdeleStyle {

}
