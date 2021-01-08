package com.example.beer.order.service.services;

import com.example.beer.order.service.domain.BeerOrder;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
