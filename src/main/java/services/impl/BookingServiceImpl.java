package services.impl;

import entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import services.AuditoriumService;
import services.BookingService;
import services.DiscountService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by macbook on 02.01.17.
 */
public class BookingServiceImpl implements BookingService {
    private final DiscountService discountService;
    private final AuditoriumService auditoriumService;
    
    private Map<Rating, Double>  multiplierByRating;
    private Map<SeatType, Double> multiplierBySeatType;
    private Map<Long, List<Ticket>> purchasedTicket;

    @Autowired
    public BookingServiceImpl(DiscountService discountService,
                              AuditoriumService auditoriumService,
                              Map<Rating, Double> multiplierByRating,
                              Map<SeatType, Double> multiplierBySeatType) {
        this.discountService = discountService;
        this.auditoriumService = auditoriumService;
        this.multiplierByRating = multiplierByRating;
        this.multiplierBySeatType = multiplierBySeatType;
    }

    @Override
    public double getTotalPrice(Event event, LocalDateTime dateTime, User user, Set<Seat> seats) {
        validateParameters(event, dateTime, seats);
        
        double basePrice = event.getBasePrice();
        basePrice = updateBasePriceDependsOnRating(basePrice, event.getRating());

        double result = calculatePriceBasedOnSeatType(seats, basePrice);

        int discount = discountService.getDiscount(user, event, dateTime, seats.size());
        result = result - (discount * result)/ 100;
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

    private double calculatePriceBasedOnSeatType(Set<Seat> seats, double basePrice) {
        double result = 0;
        for (Seat seat : seats) {
            Double multiplier = multiplierBySeatType.get(seat.getType());
            if (multiplier == null) {
                multiplier = 1.0;
            }
            result += multiplier * basePrice;
        }
        return result;
    }

    private double updateBasePriceDependsOnRating(double basePrice, Rating rating) {
        Double multiplier = multiplierByRating.get(rating);
        if (multiplier != null) {
            basePrice *= multiplier;
        }
        return basePrice;
    }

    private void validateParameters(Event event, LocalDateTime dateTime, Set<Seat> seats) {
        if (!isAuditoriumExistForEvent(event, dateTime)) {
            throw new IllegalArgumentException("There is no auditorium for this event and date");
        }

        if (!isAllSeatsAvailableToBook(event, dateTime, seats)) {
            throw new IllegalArgumentException("Some of your seats already occupied");
        }
    }

    private boolean isAllSeatsAvailableToBook(Event event, LocalDateTime dateTime, Set<Seat> seats) {
        Auditorium auditorium = auditoriumService.getAuditoriumByEventAndDate(event, dateTime);
        Set<Seat> occupiedSeats = auditoriumService.getOccupiedSeats(auditorium);
        return occupiedSeats == null || occupiedSeats.removeAll(seats);
    }

    private boolean isAuditoriumExistForEvent(Event event, LocalDateTime dateTime) {
        Auditorium auditorium = auditoriumService.getAuditoriumByEventAndDate(event, dateTime);
        return auditorium != null;
    }

}
