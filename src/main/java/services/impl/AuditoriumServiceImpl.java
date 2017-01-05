package services.impl;

import entities.Auditorium;
import services.AuditoriumService;

import java.util.List;

/**
 * Created by macbook on 02.01.17.
 */
public class AuditoriumServiceImpl implements AuditoriumService {

    private List<Auditorium> auditoriumList;

    public AuditoriumServiceImpl(List<Auditorium> auditoriumList) {
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
}
