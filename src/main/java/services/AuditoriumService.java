package services;

import entities.Auditorium;

import java.util.List;

/**
 * Created by macbook on 02.01.17.
 */
public interface AuditoriumService {
    List<Auditorium> getAll();

    Auditorium getByName(String name);
}
