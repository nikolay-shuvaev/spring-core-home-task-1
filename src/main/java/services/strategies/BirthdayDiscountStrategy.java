package services.strategies;

import entities.Event;
import entities.User;

import java.time.LocalDate;

/**
 * Created by macbook on 02.01.17.
 */
public class BirthdayDiscountStrategy extends DiscountStrategyBase {
    private int discountDays;

    public BirthdayDiscountStrategy(int discountValue, int discountDays) {
        super(discountValue);
        this.discountDays = discountDays;
    }

    public boolean isApplyDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets) {
        if (user != null) {
            LocalDate till = dateTime.plusDays(discountDays);
            LocalDate currentDateOfBirth  = user.getBirthday().withYear(dateTime.getYear());

            return (dateTime.equals(currentDateOfBirth) || dateTime.isBefore(currentDateOfBirth))
                    && (till.equals(currentDateOfBirth) || till.isAfter(currentDateOfBirth));
        }
        return false;

    }

    @Override
    public int calculateDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets) {
        return getDiscountValue();
    }

}
