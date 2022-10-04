package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.user.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.base.data.DataStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.util.UtilReader;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DbUserStorage implements DataStorage<User> {

    private final JdbcTemplate users;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql/query/user/";
    private static final String SELECT_ALL_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_all.sql");
    private static final String SELECT_BY_ID_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "select_by_id.sql");
    private static final String INSERT_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "insert.sql");
    private static final String UPDATE_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "update.sql");
    private static final String DELETE_SQL_QUERY = UtilReader.readString(SQL_QUERY_DIR + "delete.sql");

    public DbUserStorage(JdbcTemplate users) {
        this.users = users;
    }

    @Override
    public List<User> getAll() {
        return users.query(SELECT_ALL_SQL_QUERY, new UserMapper());
    }

    @Override
    public User getById(Long id) {
        return users.query(SELECT_BY_ID_SQL_QUERY, new UserMapper(), id).stream().findAny().orElse(null);
    }

    @Override
    public boolean contains(Long id) {
        return getById(id) != null;
    }

    @Override
    public User add(User object) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            users.update(connection -> {
                PreparedStatement preparedStatement = connection
                            .prepareStatement(INSERT_SQL_QUERY,
                                    Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, object.getEmail());
                preparedStatement.setString(2, object.getLogin());
                preparedStatement.setString(3, object.getName());
                preparedStatement.setDate(4, Date.valueOf(object.getBirthday()));
                return preparedStatement;

            }, keyHolder);

            return getById(keyHolder.getKey().longValue());
        } catch (DuplicateKeyException e) {
            if (e.getMessage().contains("EMAIL")) {
                throw new UserAlreadyExistException("EMAIL", object.getEmail());
            } else if (e.getMessage().contains("LOGIN")) {
                throw new UserAlreadyExistException("LOGIN", object.getLogin());
            } else {
                throw new UnsupportedOperationException(e.getMessage());
            }
        }
    }

    @Override
    public User update(User object) {
        users.update(UPDATE_SQL_QUERY,
                object.getEmail(),
                object.getLogin(),
                object.getName(),
                Date.valueOf(object.getBirthday()),
                object.getId());
        return getById(object.getId());
    }

    @Override
    public boolean delete(Long userId) {
        users.update(DELETE_SQL_QUERY,userId);
        return true;
    }
}
