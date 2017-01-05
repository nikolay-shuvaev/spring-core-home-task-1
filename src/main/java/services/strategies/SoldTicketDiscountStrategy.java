package services.strategies;

import entities.Event;
import entities.User;

import java.time.LocalDate;

/**
 * Created by macbook on 02.01.17.
 */
public class SoldTicketDiscountStrategy extends DiscountStrategyBase {
    private int discountTicketNumber;

    public SoldTicketDiscountStrategy(int discountValue, int discountTicketNumber) {
        super(discountValue);
        this.discountTicketNumber = discountTicketNumber;
    }

    public boolean isApplyDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets) {
        return numberOfTickets >= discountTicketNumber;
    }

    @Override
    public int calculateDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets) {
        int numberOfTicketsWithDiscount = numberOfTickets / discountTicketNumber;
        return (100 - (100 - getDiscountValue())) * numberOfTicketsWithDiscount / numberOfTickets;
    }

}
