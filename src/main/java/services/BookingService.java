package services;

import entities.Event;
import entities.Seat;
import entities.Ticket;
import entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by macbook on 02.01.17.
 */
public interface BookingService {
    double getTotalPrice(Event event, LocalDateTime dateTime, User user, List<Seat> seats);

    boolean bookTicket(List<Ticket> tickets);

    List<Ticket> getPurchasedTicketsForEvent(Event event, LocalDate dateTime);
}
