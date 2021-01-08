package com.example.beer.order.service.services;

import com.example.beer.order.service.bootstrap.BeerOrderBootStrap;
import com.example.beer.order.service.domain.Customer;
import com.example.beer.order.service.repositories.BeerOrderRepository;
import com.example.beer.order.service.repositories.CustomerRepository;
import com.example.brewery.model.BeerOrderDto;
import com.example.brewery.model.BeerOrderLineDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class TestingRoomService {
    private final CustomerRepository customerRepository;
    private final BeerOrderService beerOrderService;
    private final BeerOrderRepository beerOrderRepository;
    private final List<String> beerUpcs = new ArrayList<>(3);

    public TestingRoomService(CustomerRepository customerRepository, BeerOrderService beerOrderService, BeerOrderRepository beerOrderRepository) {
        this.customerRepository = customerRepository;
        this.beerOrderService = beerOrderService;
        this.beerOrderRepository = beerOrderRepository;

        beerUpcs.add(BeerOrderBootStrap.BEER_1_UPC);
        beerUpcs.add(BeerOrderBootStrap.BEER_2_UPC);
        beerUpcs.add(BeerOrderBootStrap.BEER_3_UPC);
    }

    @Transactional
    @Scheduled(fixedRate = 2000)
    public void placeTastingRoomOrder() {
        List<Customer> customers = customerRepository.findAllByCustomerNameLike(BeerOrderBootStrap.TASTING_ROOM);

        if (customers.size() == 1) {
            doPlaceOrder(customers.get(0));
        } else {
            log.error("Too many or few tasting room customer found");
        }
    }

    private void doPlaceOrder(Customer customer) {
        String beerToOrder = getRandomBeerUpc();

        BeerOrderLineDto beerOrderLineDto = BeerOrderLineDto.builder()
                .upc(beerToOrder)
                .orderQuantity(new Random().nextInt(6))
                .build();

        List<BeerOrderLineDto> beerOrderLineDtos = new ArrayList<>();
        beerOrderLineDtos.add(beerOrderLineDto);

        BeerOrderDto beerOrderDto = BeerOrderDto.builder()
                .customerId(customer.getId())
                .customerRef(UUID.randomUUID().toString())
                .beerOrderLines(beerOrderLineDtos)
                .build();

        beerOrderService.placeOrder(customer.getId(), beerOrderDto);
    }

    private String getRandomBeerUpc() {
        return beerUpcs.get(new Random().nextInt(beerUpcs.size()));
    }
}
