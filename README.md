# internshipTaskTwo
Создать парочку рест контроллеров: post, get.
Cделать jsp страницу, где будет поле для ввода url (как раз те, что будут обрабатываться контроллерами), поля для body, если нужно, поле, отображающее результат, кнопка send (UI примерный, можно менять).
Post запрос должен использовать какой-то сложный объект, например, объект customer, у которого есть Id (должен генериться базой автоматически), имя, фамилия, почта, лист телефонных номеров, время создания. Post запрос допустим будет как раз создавать customer и сохранять в postgress, Get запрос будет вытаскивать из БД customer (лучше сделать разные геты - по ID, по e-mail и т.п.)
Можно добавить фильтр аутентификации, чтобы, например, посылать запросы только под кредами admin/admin.
Развернуть на Tomcat 
Юнит-тестирование (желательно с использованием Mockito)
Логгирование

Ниже примеры адекватных запросов при использовании сервиса в качестве REST API (взаимодействие с интерфейсом UserController)
Важно: в запросах должна присутсвовать базовая авторизация с логином/паролем admin/admin зашифрованными в base64

{id:\\d+} = это id номер User'a, записанный числом

**************************
GET
Получаем список Users в формате JSON
http://localhost:8080/SpringRest/api/users

**************************
Получаем User по id в формате JSON
http://localhost:8080/SpringRest/api/user{id:\\d+}

**************************
POST
Добавляем нового User в ответ получаем добавленного User в формате JSON
URL
/SpringRest/api/add

HEADERS
Content-Type: application/json
Accept: application/json

{
    "firstName": "Tom",
    "lastName": "Tomson",
    "eMail": "tomson@tom.com",
    "phoneNumbers": [
        "88881",
        "88882",
	"88885"
    ]
}

**************************
PUT
Изменяем существующего User по id в ответ получаем измененного User в формате JSON

URL
/SpringRest/api/user/{id:\\d+}

HEADERS
Content-Type: application/json
Accept: application/json

BODY
{
    "id": 1,
    "firstName": "Denis",
    "lastName": "Denisov",
    "eMail": "denisov@tom.com",
    "phoneNumbers": [
        "77771",
        "77772",
	"77775"
    ],
    "createdOn": "2019-10-02 18:11:09"
}
***************************
DELETE
Удаляем существующего User по id 

/SpringRest/api/user/{id:\\d+}

Код для создания и первоначального наполнения БД (создавала отдельно)
CREATE TABLE IF NOT EXISTS users(
   user_id serial PRIMARY KEY,
   first_name VARCHAR (50) NOT NULL,
   last_name VARCHAR (50) NOT NULL,
   email VARCHAR (50),
   created_on TIMESTAMP (0) NOT NULL default now()
);

CREATE TABLE IF NOT EXISTS phone_numbers(
   number_id serial PRIMARY KEY,
   phone_number VARCHAR (30) NOT NULL UNIQUE,
   user_id INTEGER NOT NULL,
FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);


INSERT INTO users (first_name, last_name, email) VALUES ('Anya', 'Averina', 'averina@tom.com');
INSERT INTO users (first_name, last_name, email) VALUES ('Boris', 'Borisov', 'borisov@tom.com');
INSERT INTO users (first_name, last_name, email) VALUES ('Victor', 'Victorov', 'victorov@tom.com');
INSERT INTO users (first_name, last_name, email) VALUES ('Gvidon', 'Gvidonov', 'gvidonov@tom.com');
INSERT INTO users (first_name, last_name, email) VALUES ('Olga', 'Averina', 'averinaO@tom.com');

INSERT INTO phone_numbers (phone_number, user_id) VALUES ('11111111111', 1);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('11111111112', 1);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('11111111113', 1);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('22222222221', 2);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('33333333331', 3);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('33333333332', 3);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('44444444441', 4);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('44444444442', 4);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('44444444443', 4);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('44444444444', 4);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('55555555551', 5);
INSERT INTO phone_numbers (phone_number, user_id) VALUES ('55555555552', 5);

