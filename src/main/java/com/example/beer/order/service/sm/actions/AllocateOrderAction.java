package com.example.beer.order.service.sm.actions;

import com.example.beer.order.service.config.JmsConfig;
import com.example.beer.order.service.domain.BeerOrder;
import com.example.beer.order.service.domain.BeerOrderEventEnum;
import com.example.beer.order.service.domain.BeerOrderStatusEnum;
import com.example.beer.order.service.repositories.BeerOrderRepository;
import com.example.beer.order.service.services.BeerOrderManagerImpl;
import com.example.beer.order.service.web.mappers.BeerOrderMapper;
import com.example.brewery.model.events.AllocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AllocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {
    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        String beerOrderId = stateContext.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER).toString();
        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(beerOrderId));

        beerOrderOptional.ifPresentOrElse(beerOrder -> {
            jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_QUEUE,
                    AllocateOrderRequest.builder()
                    .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
                    .build());

            log.debug("Sent allocation request for order id: " + beerOrderId);
        }, () -> log.error("Beer Order not found"));


    }
}
