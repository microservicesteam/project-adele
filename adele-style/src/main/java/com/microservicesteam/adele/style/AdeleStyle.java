package com.microservicesteam.adele.style;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.BuilderVisibility;
import org.immutables.value.Value.Style.ImplementationVisibility;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Target({PACKAGE, TYPE})
@Retention(CLASS)
@JsonSerialize
@JsonDeserialize
@Value.Style(
    get = {"*"},
    init = "*",
    typeAbstract = {"Abstract*"},
    typeImmutable = "Immutable*",
    builder = "builder",
    build = "build",    
    visibility = ImplementationVisibility.PUBLIC,
    overshadowImplementation = true,
    builderVisibility = BuilderVisibility.PUBLIC)
public @interface AdeleStyle {
}
