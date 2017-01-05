import entities.Auditorium;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import services.AuditoriumService;

import java.util.List;

/**
 * Created by NICK on 05.01.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:services-beans.xml")
public class AuditoriumServiceTest extends TestCase {
    @Autowired
    private AuditoriumService auditoriumService;

    @Test
    public void testGetAllAuditoriums() {
        List<Auditorium> auditoriums = auditoriumService.getAll();
        assertTrue(auditoriums.size() > 0);
    }

    @Test
    public void testGetByName() {
        List<Auditorium> auditoriums = auditoriumService.getAll();
        Auditorium auditorium = auditoriums.get(0);
        Auditorium auditoriumByName = auditoriumService.getByName(auditorium.getName());
        assertEquals(auditorium.getName(), auditoriumByName.getName());
    }
}
