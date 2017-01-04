import entities.User;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import services.strategies.BirthdayDiscountStrategy;

import java.time.LocalDate;

/**
 * Created by macbook on 02.01.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:services-beans.xml")
public class BirthdayDiscountStrategyTest extends TestCase {
    @Autowired
    private BirthdayDiscountStrategy birthdayDiscountStrategy;

    public void testDiscount() {
        User user = new User(0L, "Test", LocalDate.of(1986, 5, 4), "email");
        int applyDiscount = birthdayDiscountStrategy.getDiscount(user, null, LocalDate.of(2016, 02, 02), 5);
        assertEquals(5, applyDiscount);
    }


}
