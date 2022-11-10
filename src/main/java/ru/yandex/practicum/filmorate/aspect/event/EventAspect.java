package ru.yandex.practicum.filmorate.aspect.event;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.event.EventOperationException;
import ru.yandex.practicum.filmorate.exception.event.EventTypeException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.event.DbEventStorage;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.review.DbReviewStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.Calendar;

@Component
@Aspect
public class EventAspect {

    private final EventStorage eventStorage;
    private final ReviewStorage reviewStorage;

    private static final String ADD_OPERATION = "ADD";
    private static final String REMOVE_OPERATION = "REMOVE";
    private static final String UPDATE_OPERATION = "UPDATE";
    private static final String LIKE_TYPE = "LIKE";
    private static final String REVIEW_TYPE = "REVIEW";
    private static final String FRIEND_TYPE = "FRIEND";

    public EventAspect(DbEventStorage eventStorage, DbReviewStorage reviewStorage) {
        this.eventStorage = eventStorage;
        this.reviewStorage = reviewStorage;
    }

    @Pointcut("execution(* ru.yandex.practicum.filmorate.service.film.FilmService.addLike(Long, Long))")
    public void executeEventAddLike() {
    }

    @Pointcut("execution(* ru.yandex.practicum.filmorate.service.film.FilmService.deleteLike(Long, Long))")
    public void executeEventRemoveLike() {
    }

    @Pointcut("execution(* ru.yandex.practicum.filmorate.service.user.UserService.addFriend(Long, Long))")
    public void executeEventAddFriend() {
    }

    @Pointcut("execution(* ru.yandex.practicum.filmorate.service.user.UserService.deleteFriend(Long, Long))")
    public void executeEventRemoveFriend() {
    }

    @Pointcut("execution(* ru.yandex.practicum.filmorate.service.review.ReviewService.createReview" +
            "(ru.yandex.practicum.filmorate.model.Review))")
    public void executeEventAddReview() {
    }

    @Pointcut("execution(* ru.yandex.practicum.filmorate.service.review.ReviewService.deleteReviewById(Long))")
    public void executeEventRemoveReview() {
    }

    @Pointcut("execution(* ru.yandex.practicum.filmorate.service.review.ReviewService.updateReview" +
            "(ru.yandex.practicum.filmorate.model.Review))")
    public void executeEventUpdateReview() {
    }

    @AfterReturning("executeEventAddFriend()")
    public void recordEventAddFriend(JoinPoint jp) {
        Long userId = (Long) jp.getArgs()[0];
        Long entityId = (Long) jp.getArgs()[1];
        addEvent(userId,
                entityId,
                FRIEND_TYPE,
                ADD_OPERATION);
    }

    @AfterReturning("executeEventRemoveFriend()")
    public void recordEventDeleteFriend(JoinPoint jp) {
        Long userId = (Long) jp.getArgs()[0];
        Long entityId = (Long) jp.getArgs()[1];
        addEvent(userId,
                entityId,
                FRIEND_TYPE,
                REMOVE_OPERATION);
    }

    @AfterReturning("executeEventAddLike()")
    public void recordEventAddLike(JoinPoint jp) {
        Long userId = (Long) jp.getArgs()[1];
        Long entityId = (Long) jp.getArgs()[0];
        addEvent(userId,
                entityId,
                LIKE_TYPE,
                ADD_OPERATION);
    }

    @AfterReturning("executeEventRemoveLike()")
    public void recordEventRemoveLike(JoinPoint jp) {
        Long userId = (Long) jp.getArgs()[1];
        Long entityId = (Long) jp.getArgs()[0];
        addEvent(userId,
                entityId,
                LIKE_TYPE,
                REMOVE_OPERATION);
    }

    @AfterReturning("executeEventAddReview()")
    public void recordEventAddReview(JoinPoint jp) {
        Review review = (Review) jp.getArgs()[0];
        Long userId = review.getUserId();
        Long entityId = reviewStorage.getReviewByUserIdAndFilmId(userId, review.getFilmId()).getReviewId();
        addEvent(userId,
                entityId,
                REVIEW_TYPE,
                ADD_OPERATION);
    }

    @Before("executeEventRemoveReview()")
    public void recordEventRemoveReview(JoinPoint jp) {
        Long entityId = (Long) jp.getArgs()[0];
        Long userId = reviewStorage.getReviewById(entityId).getUserId();
        addEvent(userId,
                entityId,
                REVIEW_TYPE,
                REMOVE_OPERATION);
    }

    @AfterReturning("executeEventUpdateReview()")
    public void recordEventUpdateReview(JoinPoint jp) {
        Review review = (Review) jp.getArgs()[0];
        Long entityId = review.getReviewId();
        Long userId = reviewStorage.getReviewById(entityId).getUserId();
        addEvent(userId,
                entityId,
                REVIEW_TYPE,
                UPDATE_OPERATION);
    }

    private void addEvent(Long userId, Long entityId, String eventType, String operation) {
        if (!eventStorage.getEventTypes().contains(eventType)) {
            throw new EventTypeException(eventType);
        } else if (!eventStorage.getEventOperations().contains(operation)) {
            throw new EventOperationException(operation);
        }
        eventStorage.addEvent(userId, entityId, eventType, operation, getCurrentTime());
    }

    private long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
