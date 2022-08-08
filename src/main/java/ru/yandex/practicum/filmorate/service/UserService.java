package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserService extends AbstractDataObjectService<User> {

    @Override
    protected Class<User> getClassType() {
        return User.class;
    }
}
