import entities.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import services.AuditoriumService;
import services.BookingService;
import services.EventService;
import services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static entities.SeatType.*;

/**
 * Created by NICK on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:services-beans.xml")
public class BookingServiceTest extends TestCase {
    public static final String TEST_AUDITORIUM = "Center Name 4";
    public static final LocalDateTime EVENT_DATE_TIME = LocalDateTime.of(2017, 2, 2, 13, 0);
    @Autowired
    private BookingService bookingService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private UserService userService;

    private Event testEvent;

    @Before
    public void init() {
        long eventId = eventService.save("Test Event 1", 20.0, Rating.MID);
        testEvent = eventService.getById(eventId);
        Auditorium auditorium = auditoriumService.getByName(TEST_AUDITORIUM);
        auditoriumService.addEvent(testEvent, auditorium, EVENT_DATE_TIME);
    }

    @Test
    public void testGetTicketsPriceForNonRegisterdUser() {
        double totalPrice = bookingService.getTotalPrice(testEvent, EVENT_DATE_TIME, null,
                new HashSet<>(Arrays.asList(Seat.of(11, STANDARD), Seat.of(12, STANDARD), Seat.of(13, STANDARD))));
        assertEquals(60.0, totalPrice);
    }

    @Test
    public void testGetTicketsPriceForUserWithBirthday() {
        long userId = userService.save("Test User", LocalDate.from(EVENT_DATE_TIME.plusDays(3)), "test@test.com");
        User user = userService.getById(userId);
        double totalPrice = bookingService.getTotalPrice(testEvent, EVENT_DATE_TIME, user,
                new HashSet<>(Arrays.asList(Seat.of(11, STANDARD), Seat.of(12, STANDARD), Seat.of(13, STANDARD))));
        assertEquals(54.0, totalPrice);
    }

    @Test
    public void testGetTicketsPriceForHighRatedEvent() {
        Event highRatedEvent = new Event(testEvent.getId(), testEvent.getName(), testEvent.getBasePrice(), Rating.HIGH);
        double totalPrice = bookingService.getTotalPrice(highRatedEvent, EVENT_DATE_TIME, null,
                new HashSet<>(Arrays.asList(Seat.of(11, STANDARD), Seat.of(12, STANDARD), Seat.of(13, STANDARD))));
        assertEquals(90.0, totalPrice);
    }

    @Test
    public void testGetTicketsPriceForVipSeats() {
        double totalPrice = bookingService.getTotalPrice(testEvent, EVENT_DATE_TIME, null,
                new HashSet<>(Arrays.asList(Seat.of(11, VIP), Seat.of(12, VIP), Seat.of(13, VIP))));
        assertEquals(120.0, totalPrice);
    }

}