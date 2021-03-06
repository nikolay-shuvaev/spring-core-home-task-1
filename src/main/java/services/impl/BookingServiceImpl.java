package services.impl;

import dao.PurchasedTicketDao;
import entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import services.AuditoriumService;
import services.BookingService;
import services.DiscountService;
import services.UserService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by macbook on 02.01.17.
 */
public class BookingServiceImpl implements BookingService {
    private final DiscountService discountService;
    private final AuditoriumService auditoriumService;
    private final UserService userService;
    private final PurchasedTicketDao purchasedTicketDao;

    private Map<Rating, Double>  multiplierByRating;
    private Map<SeatType, Double> multiplierBySeatType;

    @Autowired
    public BookingServiceImpl(DiscountService discountService,
                              AuditoriumService auditoriumService,
                              UserService userService,
                              PurchasedTicketDao purchasedTicketDao,
                              Map<Rating, Double> multiplierByRating,
                              Map<SeatType, Double> multiplierBySeatType) {
        this.discountService = discountService;
        this.auditoriumService = auditoriumService;
        this.userService = userService;
        this.purchasedTicketDao = purchasedTicketDao;
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
        validateTickets(tickets);

        for (Ticket ticket : tickets) {
            purchasedTicketDao.saveTicket(ticket);
            if (ticket.getUser() != null) {
                userService.addPurchasedTicket(ticket.getUser(),ticket);
            }
        }
        return true;
    }

    @Override
    public List<Ticket> getPurchasedTicketsForEvent(Event event, LocalDateTime dateTime) {
        return purchasedTicketDao.getBy(event, dateTime);
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
            throw new IllegalArgumentException("Specified seats could not be booked");
        }
    }

    private boolean isAllSeatsAvailableToBook(Event event, LocalDateTime dateTime, Set<Seat> seats) {
        Auditorium auditorium = auditoriumService.getAuditoriumByEventAndDate(event, dateTime);
        boolean isSomeSeatsOccupied = isSomeSeatsOccupied(seats, auditorium);
        boolean isSeatsSuiteToAuditorium = isSeatsSuiteToAuditorium(seats, auditorium);
        return !isSomeSeatsOccupied && isSeatsSuiteToAuditorium;
    }

    private boolean isSeatsSuiteToAuditorium(Set<Seat> seats, Auditorium auditorium) {
        Integer numberOfSeats = auditorium.getNumberOfSeats();
        Set<Integer> vipSeats = auditorium.getVipSeats();
        for (Seat seat : seats) {
            int seatNumber = seat.getSeatNumber();
            if (seatNumber > numberOfSeats || (seat.getType() == SeatType.VIP && !vipSeats.contains(seatNumber))) {
                return false;
            }
        }
        return true;
    }

    private boolean isSomeSeatsOccupied(Set<Seat> seats, Auditorium auditorium) {
        Set<Seat> occupiedSeats = auditoriumService.getOccupiedSeats(auditorium);
        return occupiedSeats != null && !occupiedSeats.removeAll(seats);
    }

    private boolean isAuditoriumExistForEvent(Event event, LocalDateTime dateTime) {
        Auditorium auditorium = auditoriumService.getAuditoriumByEventAndDate(event, dateTime);
        return auditorium != null;
    }

    private void validateTickets(List<Ticket> tickets) {
        if (tickets == null) {
            throw new IllegalArgumentException("No tickets passed");
        }

        for (Ticket ticket : tickets) {
            validateParameters(ticket.getEvent(), ticket.getDateTime(), new HashSet<>(Collections.singletonList(ticket.getSeat())));
        }
    }

}
