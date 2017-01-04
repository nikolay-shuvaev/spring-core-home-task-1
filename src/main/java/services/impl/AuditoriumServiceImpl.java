package services.impl;

import entities.Auditorium;
import services.AuditoriumService;

import java.util.List;

/**
 * Created by macbook on 02.01.17.
 */
public class AuditoriumServiceImpl implements AuditoriumService {
    private List<Auditorium> auditoriumList;

    public Auditorium getByName(String name) {
        for (Auditorium auditorium : auditoriumList) {
            if (auditorium.getName().equalsIgnoreCase(name)) {
                return auditorium;
            }
        }
        return null;
    }

    public List<Auditorium> getAll() {
        return auditoriumList;
    }

}
