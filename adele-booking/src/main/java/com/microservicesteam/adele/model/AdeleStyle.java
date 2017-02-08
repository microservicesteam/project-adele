package com.microservicesteam.adele.model;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static org.immutables.value.Value.Style.ImplementationVisibility.PUBLIC;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
    visibility = PUBLIC,
    overshadowImplementation = true,
    passAnnotations = {Entity.class, Id.class, GeneratedValue.class})
public @interface AdeleStyle {

}
