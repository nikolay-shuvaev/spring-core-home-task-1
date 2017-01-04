package services.strategies;

import entities.Event;
import entities.User;

import java.time.LocalDate;

/**
 * Created by macbook on 02.01.17.
 */
public interface DiscountStrategy {
    int getDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets);
}