package com.example.msscbeerorderservice.services;

import com.example.msscbeerorderservice.domain.BeerOrder;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
