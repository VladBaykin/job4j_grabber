[![Build Status](https://travis-ci.com/VladBaykin/job4j_grabber.svg?branch=master)](https://travis-ci.com/VladBaykin/job4j_grabber)
[![codecov](https://codecov.io/gh/VladBaykin/job4j_grabber/branch/master/graph/badge.svg?token=KH3WYHLICF)](https://codecov.io/gh/VladBaykin/job4j_grabber)
# Агрегатор вакансий
Приложение парсит вакансии с сайта sql.ru. Запускается по расписанию, считывает все вакансии, относящиеся к Java и записывает их в базу. 
Архитектура позволяет расширить его для парсинга вакансий с других сайтов.

## Технологии
- Postgresql в качестве хранилища данных
- JDBC для коннекта к БД
- Jsoup для парсинга
- Quartz для периодизации работы

![ScreenShot](screenshots/sql_parse.JPG)