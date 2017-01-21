package com.microservicesteam.adele.model;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static org.immutables.value.Value.Style.ImplementationVisibility.PUBLIC;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.immutables.value.Value;

@Target({PACKAGE, TYPE})
@Retention(CLASS)
@Value.Style(
    get = {"*"},
    init = "with*",
    typeAbstract = {"Abstract*"},
    typeImmutable = "Immutable*",
    builder = "builder",
    build = "build",    
    visibility = PUBLIC)
public @interface AdeleStyle {

}
