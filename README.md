# java-filmorate
Template repository for Filmorate project.

---

## Структура БД

Приложение **Filmorate** хранит данные в базе данных. Предпочтительная СУБД для хранения - PostgreSQL

### ER-схема
![filmorate.svg](src/main/resources/filmorate.svg)

### Описания таблиц

#### users
Таблица модели User. Хранит информацию о пользователях

- `user_id` **[PK]** - Уникальный идентификатор пользователя. Создаётся приложением
- `email` - Электронная почта пользователя. Должна быть корректным email-адресом
- `login` - Имя учётной записи пользователя
- `name` - Имя пользователя. Может быть пустым
- `birthday` - День рождения пользователя

#### films
Таблица модели Film. Хранит информацию о фильмах

- `film_id` **[PK]** - Уникальный идентификатор фильма. Создаётся приложением
- `name` - Название фильма
- `description` - Описание фильма
- `release_date` - Дата выхода фильма
- `duration` - Длительность фильма в минутах
- `genre_id` - Идентификатор жанра
- `rating_id` - Идентификатор MPA рейтинга

#### genres
Таблица привязки идентификатора жанра к его названию

- `genre_id` **[PK, FK → films.genre_id]** - Идентификатор жанра
- `name` - Название жанра, соответствующее идентификатору

#### ratings
Таблица привязки идентификатора рейтинга MPA к его обозначению

- `rating_id` **[PK, FK → films.rating_id]** - Идентификатор MPA рейтинга
- `name` - Обозначение рейтинга, соответствующее идентификатору

#### friends
Таблица для связи пользователей и их друзей.

- `origin_user_id` **[PK, FK → users.user_id]** - Идентификатор пользователя, инициировавшего заявку на добавление в друзья
- `friend_user_id` **[PK, FK → users.user_id]** - Идентификатор пользователя, ставшего целью заявки на добавление в друзья
- `accepted` - Статус заявки. false - заявка пока что не принята, true - заявка принята

#### film_likes
Таблица для связи пользователей и их лайков фильмам

- `user_id` **[PK, FK → users.user_id]** - Идентификатор пользователя, поставившего фильму лайк
- `film_id` **[PK, FK → films.film_id]** - Идентификатор фильма, которому был поставлен лайк

### Примеры запросов

#### Получение списка всех фильмов
```sql
SELECT  f.film_id,
        f.name,
        f.description,
        f.release_date,
        f.duration,
        g.name AS genre,
        r.name AS rating
FROM    films AS f
LEFT JOIN genres AS g ON f.genre_id=g.genre_id
LEFT JOIN ratings AS r ON f.rating_id=r.rating_id;
```

#### Получение списка 10 самых лайкнутых фильмов
```sql
SELECT  f.*
FROM films AS f
LEFT JOIN film_likes AS fl ON f.film_id=fl.film_id
GROUP BY f.film_id
ORDER BY COUNT(fl.user_id) DESC
LIMIT 10;
```

#### Получение списка всех подтвержденных друзей пользователя
```sql
SELECT u.*
FROM users AS u
JOIN friends AS fr  ON (u.user_id=fr.friend_user_id AND fr.origin_user_id = :id)
                    OR (u.user_id=fr.origin_user_id AND fr.friend_user_id = :id)
WHERE fr.accepted = true;
```

#### Получение списка общих друзей
```sql
SELECT u.*
FROM users AS u
JOIN friends AS fr1   ON (u.user_id=fr1.friend_user_id AND fr1.origin_user_id = 3)
                      OR (u.user_id=fr1.origin_user_id AND fr1.friend_user_id = 3)
JOIN friends AS fr2   ON (u.user_id=fr2.friend_user_id AND fr2.origin_user_id = 2)
                      OR (u.user_id=fr2.origin_user_id AND fr2.friend_user_id = 2)
WHERE fr1.accepted = true
AND fr2.accepted = true;
```