[![Build Status](https://travis-ci.com/VladBaykin/job4j_grabber.svg?branch=master)](https://travis-ci.com/VladBaykin/job4j_grabber)
[![codecov](https://codecov.io/gh/VladBaykin/job4j_grabber/branch/master/graph/badge.svg?token=KH3WYHLICF)](https://codecov.io/gh/VladBaykin/job4j_grabber)
# Агрегатор вакансий
Приложение парсит вакансии с сайта sql.ru. Запускается по расписанию, считывает все вакансии, относящиеся к Java и записывает их в базу. 
Архитектура позволяет расширить его для парсинга вакансий с других сайтов.

## Технологии
- Java 15
- Postgresql
- JDBC
- Jsoup
- Quartz

![ScreenShot](screenshots/sql_parse.JPG)