# java-filmorate
Template repository for Filmorate project.

## Диаграмма по спринту 11
![diagram](/diagram.png)

###### Примеры запроса к оснвным таблицам
SELECT 
      film_id,
      name,
      description,
      releaseDate,
      duration,
      rate,
      MPA
FROM films

SELECT 
      user_id,
      email,
      login,
      name,
      birthday
FROM users
