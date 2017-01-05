package services.impl;

import entities.Event;
import entities.User;
import services.DiscountService;
import services.strategies.DiscountStrategy;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by macbook on 02.01.17.
 */
public class DiscountServiceImpl implements DiscountService {
    private List<DiscountStrategy> strategies;

    public int getDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets) {
        int result = 0;
        for (DiscountStrategy strategy : strategies) {
            int discount = strategy.getDiscount(user, event, dateTime, numberOfTickets);
            if (discount > result) {
                result = discount;
            }
        }
        return result;
    }

    public void setStrategies(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }
}
