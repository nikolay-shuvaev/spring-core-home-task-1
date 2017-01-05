package entities;

import java.util.Set;

/**
 * Created by macbook on 02.01.17.
 */
public class Auditorium {
    private String name;
    private String numberOfSeats;
    private Set<String> vipSeats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfSeats(String numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setVipSeats(Set<String> vipSeats) {
        this.vipSeats = vipSeats;
    }

    public Set<String> getVipSeats() {
        return vipSeats;
    }
}
