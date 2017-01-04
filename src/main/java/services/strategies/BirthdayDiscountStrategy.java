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
            LocalDate now = LocalDate.now();
            LocalDate till = now.plusDays(discountDays);
            LocalDate currentDateOfBirth  = user.getBirthday().withYear(now.getYear());

            return (now.equals(currentDateOfBirth) || now.isBefore(currentDateOfBirth))
                    && (till.equals(currentDateOfBirth) || till.isAfter(currentDateOfBirth));
        }
        return false;

    }

    @Override
    public int calculateDiscount(User user, Event event, LocalDate dateTime, int numberOfTickets) {
        return getDiscountValue();
    }

}
