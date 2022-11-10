package ru.yandex.practicum.filmorate.service.director;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.base.DataObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.base.DataObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<Director> getAll(){
        return directorStorage.getDirectorList();
    }

    public Director getById(Integer directorId){
        if (!contains(directorId)) {
            throw new DataObjectNotFoundException(directorId.longValue());
        }
        return directorStorage.getDirectorById(directorId);
    }

    public Director add(Director director){
        if (contains(director.getId())) {
            throw new DataObjectAlreadyExistException("Такой режиссёр уже существует");
        }
        return directorStorage.add(director);
    }

    public Director update(Director director){
        if (!getAll().contains(director)) {
            throw new DataObjectNotFoundException(director.getId().longValue());
        }
        return directorStorage.update(director);
    }

    public boolean delete(Integer directorId){
        if (!contains(directorId)) {
            throw new DataObjectNotFoundException(directorId.longValue());
        }
        return directorStorage.delete(directorId);
    }

    public boolean contains(Integer directorId) {
        return directorStorage.contains(directorId);
    }
}
