package com.example.msscbeerorderservice.sm.actions;

import com.example.model.events.ValidateOrderRequest;
import com.example.msscbeerorderservice.config.JmsConfig;
import com.example.msscbeerorderservice.domain.BeerOrder;
import com.example.msscbeerorderservice.domain.BeerOrderStatusEnum;
import com.example.msscbeerorderservice.repositories.BeerOrderRepository;
import com.example.msscbeerorderservice.services.BeerOrderManagerImpl;
import com.example.msscbeerorderservice.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderStatusEnum> {
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderStatusEnum> stateContext) {
        String beerOrderId = (String) stateContext.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        BeerOrder beerOrder = beerOrderRepository.findOneById(UUID.fromString(beerOrderId));

        jmsTemplate.convertAndSend(
                JmsConfig.VALIDATE_ORDER_QUEUE,
                ValidateOrderRequest.builder()
                    .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
        );

        log.debug("Sent Validation request to queue for order id: " + beerOrderId);
    }
}
