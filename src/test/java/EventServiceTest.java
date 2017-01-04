import entities.Event;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import services.EventService;

import java.time.LocalDate;

/**
 * Created by macbook on 04.01.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:services-beans.xml")
public class EventServiceTest extends TestCase {
    private static final String TEST_EVENT_1 = "Test Event 1";
    private static final String TEST_EMAIL = "test1@test.com";
    @Autowired
    private EventService EventService;

    @Test
    public void testRegisterEvent() {
        long id = EventService.save(TEST_EVENT_1, LocalDate.of(1999, 11, 11), TEST_EMAIL);
        assertTrue("Id is more than 0", id > 0);
        Event returnedById = EventService.getById(id);
        assertEquals(TEST_EVENT_1, returnedById.getName());
    }

    @Test
    public void testGetEventById() {
        Event EventByEmail = EventService.getEventByEmail(TEST_EMAIL);
        assertEquals(TEST_EMAIL, EventByEmail.getEmail());
    }

    @Test
    public void testRemoveEvent() {
        int initialSize = EventService.getAll().size();
        long id = EventService.save("Test Event 2", LocalDate.of(1999, 11, 11), "test2@test.com");
        assertEquals(initialSize + 1, EventService.getAll().size());
        EventService.remove(id);
        assertEquals(initialSize, EventService.getAll().size());
    }
}
