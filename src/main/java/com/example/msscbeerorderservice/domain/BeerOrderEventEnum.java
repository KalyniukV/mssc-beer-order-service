package com.example.msscbeerorderservice.domain;

public enum BeerOrderEventEnum {
    VALIDATE_ORDER,
    VALIDATION_PASSED,
    VALIDATION_FAILED,
    ALLOCATED_ORDER,
    ALLOCATED_SUCCESS,
    ALLOCATION_NO_INVENTORY,
    ALLOCATION_FAILED,
    BEERORDER_PICKED_UP
}
