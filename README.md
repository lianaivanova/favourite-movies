# Favourite Movies - Mobile Application

Приложението представлява списък от любими филми. 
Филмите могат да се добавят, редактират, да се маркират като гледани или да се оценяват, и да се изтиват. (CRUD Operations)

Когато се добавя нов филм, при изписването на име на филм в даденото поле се прави извикване към API (https://imdb-api.com/api#Search-header) за търсене на филм с това заглавие в website-а IMDB. 

Полетата **Заглавие**, **Описание** и **Година** се попълват автоматично на база на първия филм, получен като отговор от извиканото API.
Също така се изобразява снимката, която и е корица на дадения филм.

Използвана е база данни SQLite3, като всеки филм преставлява отделен запис в таблицата:

| ID | IMDB_ID | MOVIE_TITLE | WATCHED | RATING |
| :--: | :---: | :---------: | :-----: | :----: |

IMDB_ID - уникално ID, чрез което при извикване на използваното API се връща информацията за филма с това ID.

### Начална страница - Списък от филми

![Начална страница](https://i.ibb.co/PTfkDq5/242595059-2117411695064271-8831292347563545090-n.png)

### Добави/редактирай филм

![Добави/редактирай филм](https://i.ibb.co/qDqnN4L/242621323-851593322190587-4152634360892679392-n.png)
