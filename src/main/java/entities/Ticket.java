package entities;

import sun.security.x509.X509CertInfo;

import java.time.LocalDate;

/**
 * Created by macbook on 02.01.17.
 */
public class Ticket {
    private User user;
    private Event event;
    private LocalDate dateTime;
    private Seat seat;

    public Ticket(User user, Event event, LocalDate dateTime, Seat seat) {
        this.user = user;
        this.event = event;
        this.dateTime = dateTime;
        this.seat = seat;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public Seat getSeat() {
        return seat;
    }
}
