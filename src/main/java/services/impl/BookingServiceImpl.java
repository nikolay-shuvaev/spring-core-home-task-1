package services.impl;

import entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import services.BookingService;
import services.DiscountService;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by macbook on 02.01.17.
 */
public class BookingServiceImpl implements BookingService {
    @Autowired
    private DiscountService discountService;
    private Map<Rating, Double>  multiplierByRating;
    private Map<SeatType, Double> multiplierBySeatType;
    private Map<Long, List<Ticket>> purchasedTicket;

    @Override
    public double getTotalPrice(Event event, LocalDate dateTime, User user, List<Seat> seats) {
        int discount = discountService.getDiscount(user, event, dateTime, seats.size());
        double basePrice = event.getBasePrice();
        Rating rating = event.getRating();

        double result = 0;
        if (Rating.HIGH == rating) {
            Double multiplier = multiplierByRating.get(rating);
            if (multiplier != null) {
                basePrice *= multiplier;
            }
        }
        for (Seat seat : seats) {
            if (seat.getType() == SeatType.VIP) {
                Double multiplier = multiplierBySeatType.get(SeatType.VIP);
                if (multiplier != null) {
                    result += multiplier*basePrice;
                } else {
                    result += basePrice;
                }
            }
        }

        result = result - (discount * 100)/ result;
        return result;
    }

    @Override
    public boolean bookTicket(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            long id = ticket.getEvent().getId();
            if (!purchasedTicket.containsKey(id)) {
                purchasedTicket.put(id, new ArrayList<>(Collections.singletonList(ticket)));
            } else {
                purchasedTicket.get(id).add(ticket);
            }
        }
        return false;
    }

    @Override
    public List<Ticket> getPurchasedTicketsForEvent(Event event, LocalDate dateTime) {
        List<Ticket> tickets = purchasedTicket.get(event.getId());
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getDateTime().equals(dateTime)) {
                result.add(ticket);
            }
        }
        return result;
    }

}
