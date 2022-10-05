-- список идентификаторов фильмов максимально похожего пользователя
SELECT FL3.FILM_ID
                    -- список id фильмов максимально похожего пользователя
                     FROM FILM_LIKES FL3
                     WHERE FL3.USER_ID = (SELECT FL2.USER_ID
                     -- id пользователя максимально похожего
                                          FROM FILM_LIKES FL2
                                          WHERE FL2.USER_ID <> ?
                                            AND FL2.FILM_ID IN (SELECT FL1.FILM_ID
                                          -- список фильмов проверяемого пользователя
                                                                FROM FILM_LIKES FL1
                                                                WHERE FL1.USER_ID = ?
                                              )
                                          GROUP BY FL2.USER_ID
                                          ORDER BY COUNT(FL2.FILM_ID) DESC
                                          LIMIT 1
                         )
