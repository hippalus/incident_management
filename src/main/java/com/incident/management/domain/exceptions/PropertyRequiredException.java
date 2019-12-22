package com.incident.management.domain.exceptions;


public class PropertyRequiredException extends RuntimeException {

    public PropertyRequiredException(String className, String property) {
      super(String.format("In this %s class property %s cannot be null !", className, property));
    }

}
