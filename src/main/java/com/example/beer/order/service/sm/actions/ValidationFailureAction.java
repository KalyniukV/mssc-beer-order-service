package com.example.beer.order.service.sm.actions;

import com.example.beer.order.service.domain.BeerOrderEventEnum;
import com.example.beer.order.service.domain.BeerOrderStatusEnum;
import com.example.beer.order.service.services.BeerOrderManagerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidationFailureAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        String beerOrderId = stateContext.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER).toString();
        log.error("Compensating transaction... Validation Failed: " + beerOrderId);
    }
}
