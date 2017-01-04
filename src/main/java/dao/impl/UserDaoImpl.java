package dao.impl;

import dao.UserDao;
import entities.User;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by macbook on 03.01.17.
 */
public class UserDaoImpl implements UserDao {
    private long currentId = 1;
    private Map<Long, User> users = new HashMap<>();

    @Override
    public long save(String name, LocalDate birthday, String email) {
        User user = new User(currentId++, name, birthday, email);
        Long id = user.getId();
        users.put(id, user);
        return id;
    }

    @Override
    public void remove(long id) {
        users.remove(id);
    }

    @Override
    public User getById(long id) {
        return users.get(id);
    }

    @Override
    public User getUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
