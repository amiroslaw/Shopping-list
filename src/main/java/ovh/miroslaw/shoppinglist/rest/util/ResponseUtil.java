/* Copyright 2016-2019 the original author or authors from the JHipster project.
    * Licensed under the Apache License, Version 2.0 (the "License");
 */
package ovh.miroslaw.shoppinglist.rest.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * Utility class for response entity.
 */
public interface ResponseUtil {

    /**
     * Wraps object with {@link ResponseEntity} or if it is {@code null} returns {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}.
     *
     * @param <X> the type parameter
     * @param maybeResponse the maybe response
     * @return the response entity
     */
    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    /**
     * Wraps object with {@link ResponseEntity} or if it is {@code null} returns {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}.
     *
     * @param <X> the type parameter
     * @param maybeResponse the maybe response
     * @param header the header
     * @return the response entity
     */
    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
