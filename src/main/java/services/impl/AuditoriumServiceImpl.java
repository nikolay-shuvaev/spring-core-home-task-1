package services.impl;

import dao.OccupiedSeatsDao;
import dao.ScheduleTableDao;
import entities.Auditorium;
import entities.Event;
import entities.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import services.AuditoriumService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by macbook on 02.01.17.
 */
public class AuditoriumServiceImpl implements AuditoriumService {
    private ScheduleTableDao scheduleTableDao;
    private OccupiedSeatsDao occupiedSeatsDao;
    private List<Auditorium> auditoriumList;

    @Autowired
    public AuditoriumServiceImpl(ScheduleTableDao scheduleTableDao, OccupiedSeatsDao occupiedSeatsDao, List<Auditorium> auditoriumList) {
        this.scheduleTableDao = scheduleTableDao;
        this.occupiedSeatsDao = occupiedSeatsDao;
        this.auditoriumList = auditoriumList;
    }

    @Override
    public List<Auditorium> getAll() {
        return auditoriumList;
    }

    @Override
    public Auditorium getByName(String name) {
        if (name != null) {
            for (Auditorium auditorium : auditoriumList) {
                if (name.equals(auditorium.getName())) {
                    return auditorium;
                }
            }
        }
        return null;
    }

    @Override
    public Auditorium addEvent(Event event, Auditorium auditorium, LocalDateTime dateTime) {
        Auditorium auditoriumByEventAndDate = getAuditoriumByEventAndDate(event, dateTime);
        if (auditoriumByEventAndDate == null) {
            return scheduleTableDao.addEventToAuditorium(event, auditorium, dateTime);
        }
        return null;
    }

    @Override
    public Auditorium getAuditoriumByEventAndDate(Event event, LocalDateTime dateTime) {
        return scheduleTableDao.getAuditoriumByEventAndDate(event, dateTime);
    }

    @Override
    public Set<Seat> getOccupiedSeats(Auditorium auditorium) {
        return occupiedSeatsDao.getOccupiedSeats(auditorium);
    }
}
