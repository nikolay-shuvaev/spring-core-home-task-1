package services.strategies;

import entities.Event;
import entities.User;

import java.time.LocalDate;

/**
 * Created by macbook on 02.01.17.
 */
public abstract class DiscountStrategyBase implements DiscountStrategy {
    private int discountValue;

    public DiscountStrategyBase(int discountValue) {
        this.discountValue = discountValue;
    }

    public int getDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets) {
        return isApplyDiscount(user, event, dateTime, numberOfTickets) ? calculateDiscount(user, event, dateTime, numberOfTickets) : 0;
    }

    public abstract boolean isApplyDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets);

    public abstract int calculateDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets);

    public int getDiscountValue() {
        return discountValue;
    }
}
