package com.example.beer.order.service.services.listeners;

import com.example.beer.order.service.config.JmsConfig;
import com.example.beer.order.service.services.BeerOrderManager;
import com.example.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidationResultListener {
    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE)
    public void listener(ValidateOrderResult result) {
        final UUID beerOrderId = result.getOrderId();

        log.debug("Validation result for order id: " + beerOrderId);

        beerOrderManager.processValidationResult(beerOrderId, result.getIsValid());
    }
}
