# PFLB TEAM 1

## О проекте

Проект разработан в рамках командного дипломного проекта по автоматизации тестирования учебного полигона **PFLB Test-API**.

Цель проекта — реализовать поддерживаемый автотестовый фреймворк, который покрывает основные пользовательские сценарии через:

* UI;
* API;
* DB-проверки;
* Allure-отчётность;
* запуск в Jenkins.

Проект построен так, чтобы новый участник команды мог быстро разобраться в структуре, понять назначение классов и добавить новые тесты по общей архитектуре.

---

## Технологический стек

| Направление            | Технологии    |
| ---------------------- | ------------- |
| Язык программирования  | Java          |
| Сборка проекта         | Maven         |
| UI-автоматизация       | Selenide      |
| API-автоматизация      | RestAssured   |
| Фреймворк тестирования | TestNG        |
| Отчётность             | Allure Report |
| Логирование            | Log4j2        |
| Работа с БД            | JDBC          |
| Библиотеки             | Lombok        |
| CI/CD                  | Jenkins       |

---

## Состав команды и зоны ответственности

| Участник            | Зона ответственности                             |
| ------------------- | ------------------------------------------------ |
| Вейт Владимир       | Houses: Read all, Read one by ID, Create new     |
| Алексеев Данил      | Cars: Read all, Create new                       |
| Кесаева Валерия     | Users: Read all, Read user with cars, Create new |
| Артемьева Анастасия | Users: Add money                                 |
| Горев Андрей        | Users: Buy or sell car, Settle to house          |
| Богатыренко Лидия   | All POST, All DELETE               |

---

## Основные сущности приложения

| Сущность | Описание     |
| -------- | ------------ |
| Users    | Пользователи |
| Cars     | Машины       |
| Houses   | Дома         |

Основные действия в системе:

* авторизация;
* создание пользователя;
* получение списка пользователей;
* получение пользователя с машинами;
* добавление денег пользователю;
* покупка и продажа машины;
* заселение пользователя в дом;
* создание машины;
* создание дома;
* удаление сущностей.

---

## Архитектура проекта

```text
src
├── main
│   └── java
│       ├── api
│       │   ├── adapters
│       │   │   ├── base
│       │   │   │   └── BaseAdapter.java
│       │   │   ├── cars
│       │   │   │   └── CarAdapter.java
│       │   │   ├── houses
│       │   │   │   └── HouseAdapter.java
│       │   │   └── users
│       │   │       └── UsersAdapter.java
│       │   │
│       │   └── models
│       │       ├── cars
│       │       ├── houses
│       │       └── users
│       │
│       ├── db
│       │   ├── DbConnection.java
│       │   └── UsersDbClient.java
│       │
│       ├── services
│       │   └── annotations
│       │       └── NoLogin.java
│       │
│       ├── ui
│       │   ├── dto
│       │   │   ├── Car.java
│       │   │   ├── CarFactory.java
│       │   │   └── NewHouse.java
│       │   │
│       │   ├── pages
│       │   │   ├── BasePage.java
│       │   │   ├── LoginPage.java
│       │   │   ├── CarsPage.java
│       │   │   ├── HousePage.java
│       │   │   ├── PlusMoneyPage.java
│       │   │   ├── ReadUserInfoPage.java
│       │   │   └── UsersPage.java
│       │   │
│       │   └── wrappers
│       │       ├── ButtonPush.java
│       │       ├── GetNewIdNumber.java
│       │       ├── GetRow.java
│       │       ├── GetStatus.java
│       │       ├── Input.java
│       │       ├── RadioButton.java
│       │       ├── RadioNoTitle.java
│       │       ├── Select.java
│       │       └── TableValue.java
│       │
│       └── utils
│           └── PropertyReader.java
│
└── test
    └── java
        ├── api
        │   ├── cars
        │   ├── houses
        │   └── users
        │
        ├── base
        │   ├── BaseTest.java
        │   └── LogTest.java
        │
        ├── db
        │   ├── cars
        │   └── houses
        │
        ├── listeners
        │   ├── AnnotationTransformer.java
        │   ├── ScreenshotListener.java
        │   └── TestListener.java
        │
        ├── ui
        │   ├── cars
        │   ├── houses
        │   ├── login
        │   └── user
        │
        └── utils
            ├── Retry.java
            └── UserTestDataFactory.java
```

---

## Общая логика архитектуры

| Слой         | Назначение                                                          |
| ------------ | ------------------------------------------------------------------- |
| Tests        | Описывают тестовый сценарий и проверки                              |
| Page Objects | Описывают UI-страницы и действия пользователя                       |
| Wrappers     | Переиспользуемые действия с элементами UI                           |
| Adapters     | Отправляют API-запросы                                              |
| Models  | Описывают request и response модели                                 |
| DB clients   | Выполняют SQL-запросы и DB-проверки                                 |
| Listeners    | Логируют ход выполнения тестов, делают скриншоты, подключают Allure |
| Jenkinsfile  | Описывает запуск тестов в Jenkins                                   |

---

## API

### Расположение

```text
src/main/java/api/adapters
```

API-слой отвечает за работу с backend через RestAssured.

В проекте используется паттерн **Adapter**.

Adapter нужен, чтобы не писать API-запросы напрямую в тестах.
Тест вызывает готовый метод адаптера, а адаптер уже отправляет запрос и возвращает response.

Пример цепочки:

```text
Test → Adapter → API → Response → Assert
```

---

## BaseAdapter

### Расположение

```text
src/main/java/api/adapters/base/BaseAdapter.java
```

`BaseAdapter` содержит общую настройку API-запросов:

* базовый API URL;
* Bearer token;
* content type;
* request specification;
* response specification;
* общие методы для авторизованных и неавторизованных запросов.

### Основные методы BaseAdapter

| Метод                     | Назначение                                                                 |
| ------------------------- | -------------------------------------------------------------------------- |
| `baseRequest()`           | Базовый request без Bearer token. Используется в основном для GET-запросов |
| `authorizedRequest()`     | Request с Bearer token                                                     |
| `authorizedJsonRequest()` | Request с Bearer token и `Content-Type: application/json`                  |
| `spec`                    | Общая спецификация для house-запросов                                      |
| `specCar`                 | Общая спецификация для car-запросов                                        |
| `ok200`                   | Response specification для статуса 200                                     |
| `ok201`                   | Response specification для статуса 201                                     |
| `ok202`                   | Response specification для статуса 202                                     |
| `ok204`                   | Response specification для статуса 204                                     |

### Когда что использовать

| Ситуация                | Что использовать          |
| ----------------------- | ------------------------- |
| GET без тела запроса    | `baseRequest()`           |
| DELETE с авторизацией   | `authorizedRequest()`     |
| POST/PUT с JSON body    | `authorizedJsonRequest()` |
| Проверка ожидаемого 200 | `ok200`                   |
| Проверка ожидаемого 201 | `ok201`                   |
| Проверка ожидаемого 204 | `ok204`                   |

Пример:

```java
return authorizedJsonRequest()
        .body(request)
        .when()
        .post("/user")
        .then()
        .extract()
        .response();
```

---

## UsersAdapter

### Расположение

```text
src/main/java/api/adapters/users/UsersAdapter.java
```

`UsersAdapter` отвечает за API-запросы по пользователям.

### Основные методы

| Метод                                            | Endpoint                  | Назначение                                                 |
|--------------------------------------------------| ------------------------- | ---------------------------------------------------------- |
| `createUser(UserCreateRequest request)`          | `POST /user`              | Создаёт пользователя и возвращает `Response`               |
| `createUserAndGetDto(UserCreateRequest request)` | `POST /user`              | Создаёт пользователя и возвращает DTO `UserCreateResponse` |
| `getAllUsers()`                                  | `GET /users`              | Получает список всех пользователей                         |
| `getUserById(Integer userId)`                    | `GET /user/{userId}`      | Получает пользователя по id                                |
| `getUserInfoById(Integer userId)`                   | `GET /user/{userId}/info` | Получает пользователя с машинами                           |
| `deleteUser(Integer userId)`                        | `DELETE /user/{userId}`   | Удаляет пользователя                                       |

### Где используется

`UsersAdapter` используется:

* в API-тестах Users;
* в UI/E2E-тестах Users для подготовки данных;
* в cleanup после тестов.

Пример:

```java
UserCreateRequest request = UserTestDataFactory.createValidUser();

UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);

usersAdapter.deleteUser(createdUser.getId())
        .then()
        .statusCode(204);
```

---

## Models

### Расположение

```text
src/main/java/api/models
```

Models нужны, чтобы не писать JSON руками.

DTO описывают:

* request body;
* response body;
* DB entity.

### Users models

| Модель               | Назначение                                          |
| -------------------- | --------------------------------------------------- |
| `UserCreateRequest`  | Request body для создания пользователя              |
| `UserCreateResponse` | Response body после создания пользователя           |
| `UserGetResponse`    | Response body для получения пользователя            |
| `UserInfoResponse`   | Response body для получения пользователя с машинами |
| `UserDbEntity`       | Модель строки из таблицы `person`                   |

Пример создания request:

```java
UserCreateRequest request = UserTestDataFactory.createValidUser();
```

Пример проверки response:

```java
assertEquals(createdUser.getFirstName(), request.getFirstName());
assertEquals(createdUser.getSecondName(), request.getSecondName());
assertEquals(createdUser.getAge(), request.getAge());
```

---

## UI

### Расположение

```text
src/main/java/ui/pages
```

UI-слой построен на паттерне **Page Object**.

Page Object описывает страницу приложения:

* уникальный элемент страницы;
* открытие страницы;
* действия пользователя;
* проверки результата;
* работу с wrappers.

Тест не должен напрямую знать сложные локаторы.
Тест должен вызывать понятные методы страницы.

Пример:

```java
readUserInfoPage
        .openPage()
        .shouldBeOpened()
        .setUserId(createdUserId)
        .clickRead()
        .shouldHaveUser(
                String.valueOf(createdUserId),
                createdUser.getFirstName(),
                createdUser.getSecondName()
        );
```

---

## BasePage

### Расположение

```text
src/main/java/ui/pages/BasePage.java
```

`BasePage` — базовый класс для UI-страниц.

### Основные методы

| Метод                 | Назначение                                                           |
| --------------------- | -------------------------------------------------------------------- |
| `getUniqueElement()`  | Абстрактный метод. Каждая страница указывает свой уникальный элемент |
| `waitForPageLoaded()` | Ждёт, что уникальный элемент страницы виден и активен                |
| `isPageOpened()`      | Проверяет, открыта ли страница                                       |

### Для чего нужен BasePage

`BasePage` нужен, чтобы все страницы одинаково проверяли загрузку.

Пример логики:

```java
protected abstract SelenideElement getUniqueElement();

public BasePage waitForPageLoaded() {
    getUniqueElement().shouldBe(visible, enabled);
    return this;
}
```

---

## LoginPage

### Расположение

```text
src/main/java/ui/pages/LoginPage.java
```

`LoginPage` отвечает за авторизацию.

### Основные методы

| Метод                                 | Назначение                            |
| ------------------------------------- | ------------------------------------- |
| `open()`                              | Открывает главную страницу приложения |
| `login(String user, String password)` | Вводит логин, пароль и нажимает GO    |

### Важно

В обычных UI-тестах логин руками писать не нужно.

Логин выполняется автоматически в `BaseTest`, если тестовый метод не помечен аннотацией `@NoLogin`.

---

## BaseTest для UI-тестов

### Расположение

```text
src/test/java/base/BaseTest.java
```

`BaseTest` отвечает за подготовку UI-теста.

Он выполняет:

* настройку Selenide;
* настройку браузера;
* подключение AllureSelenide;
* создание Page Object;
* автоматический логин перед тестом;
* закрытие браузера после теста.

### Что настраивается в BaseTest

```java
Configuration.timeout = 15000;
Configuration.baseUrl = "http://82.142.167.37:4881";
Configuration.clickViaJs = true;
Configuration.headless = false;
```

### Как работает логин

По умолчанию перед каждым UI-тестом выполняется:

```java
loginPage.open()
        .login(user, password)
        .waitForPageLoaded();
```

Если тесту логин не нужен, используется аннотация `@NoLogin`.

---

## Аннотация NoLogin

### Расположение

```text
src/main/java/services/annotations/NoLogin.java
```

`@NoLogin` используется для UI-тестов, где не нужна предварительная авторизация.

Например:

* тест страницы логина;
* тест проверки неавторизованного состояния.

Пример:

```java
@NoLogin
@Test
public void loginPageShouldBeOpenedTest() {
    loginPage
            .open()
            .waitForPageLoaded();
}
```

Если тест проверяет обычную страницу приложения после авторизации, `@NoLogin` ставить не нужно.

---

## Wrappers

### Расположение

```text
src/main/java/ui/wrappers
```

Wrappers — это обёртки над типовыми UI-элементами.

Они нужны, чтобы не дублировать одинаковые действия с элементами в Page Object и тестах.

---

## ButtonPush

### Расположение

```text
src/main/java/ui/wrappers/ButtonPush.java
```

### Метод

```java
ButtonPush.clickPush();
```

### Назначение

Нажимает кнопку:

```text
PUSH TO API
```

Используется на страницах создания сущностей, где после заполнения формы нужно отправить данные в API.

Пример:

```java
ButtonPush.clickPush();
```

---

## GetNewIdNumber

### Расположение

```text
src/main/java/ui/wrappers/GetNewIdNumber.java
```

### Метод

```java
GetNewIdNumber.getNewIdHouse();
```

### Назначение

Получает id созданного дома из кнопки/элемента с текстом:

```text
New house ID:
```

Метод ждёт появления текста `New house ID:` и возвращает только номер id.

Пример:

```java
String newHouseId = GetNewIdNumber.getNewIdHouse();
```

---

## GetRow

### Расположение

```text
src/main/java/ui/wrappers/GetRow.java
```

### Метод

```java
GetRow.getRowIdByIndex(String index);
```

### Назначение

Ищет строку таблицы, где в первой ячейке есть переданное значение, и возвращает id из первой колонки.

Пример:

```java
String id = GetRow.getRowIdByIndex("20");
```

---

## GetStatus

### Расположение

```text
src/main/java/ui/wrappers/GetStatus.java
```

### Метод

```java
GetStatus.getStatus();
```

### Назначение

Возвращает текст из кнопки/элемента со статусом.

Например:

```text
Status: 200 ok
Status: not pushed
```

Пример:

```java
String status = GetStatus.getStatus();
assertTrue(status.contains("200"));
```

---

## Input

### Расположение

```text
src/main/java/ui/wrappers/Input.java
```

`Input` — wrapper для работы с полями ввода.

### Методы

| Метод                                                      | Назначение                                               |
| ---------------------------------------------------------- | -------------------------------------------------------- |
| `writeLogin(String fieldName, String text)`                | Заполняет поле логина или пароля по атрибуту `name`      |
| `writeById(String id, String text)`                        | Заполняет input по `id`                                  |
| `fillByColumnName(String columnName, String value)`        | Заполняет input в таблице по названию колонки            |
| `fillByColumnNameParking(String columnName, String value)` | Заполняет input в parking-таблице                        |
| `inputRead(String text)`                                   | Вводит значение в поле read-формы и нажимает кнопку Read |

### Пример использования для логина

```java
Input.writeLogin("email", "user@pflb.ru");
Input.writeLogin("password", "user");
```

### Пример использования по id

```java
Input.writeById("first_name_send", "Ivan");
```

### Пример использования для Read-страниц

```java
Input.inputRead("20");
```

`inputRead` удобно использовать на страницах, где есть:

* поле для id;
* кнопка Read.

---

## RadioButton

### Расположение

```text
src/main/java/ui/wrappers/RadioButton.java
```

### Методы

| Метод                                          | Назначение                                  |
| ---------------------------------------------- | ------------------------------------------- |
| `selectRadio(String fieldName, String value)`  | Выбирает radio по названию поля и значению  |
| `selectRadioByName(String name, String value)` | Выбирает radio по атрибуту `name` и `value` |

Пример:

```java
RadioButton.selectRadioByName("sex", "MALE");
```

---

## RadioNoTitle

### Расположение

```text
src/main/java/ui/wrappers/RadioNoTitle.java
```

### Метод

```java
RadioNoTitle.selectRadioButton(String radioValue);
```

### Назначение

Выбирает radio button без заголовка, например для сценариев settle/evict.

Пример:

```java
RadioNoTitle.selectRadioButton("settle");
```

---

## Select

### Расположение

```text
src/main/java/ui/wrappers/Select.java
```

### Метод

```java
Select.selectByText(String dropdownText, String optionText);
```

### Назначение

Открывает dropdown по тексту и выбирает опцию по тексту.

Пример:

```java
Select.selectByText("Users", "Create new");
```

---

## TableValue

### Расположение

```text
src/main/java/ui/wrappers/TableValue.java
```

`TableValue` используется для получения значений из таблиц.

### Метод

```java
TableValue.getAllValues(String tableClass, String columnName);
```

### Как работает

Метод принимает:

```text
1. CSS class таблицы
2. Название столбца
```

И возвращает список значений из выбранного столбца.

Пример:

```java
List<String> ids = TableValue.getAllValues("tableUser", "ID");
```

### Константы таблиц

В проекте уже есть константы:

```java
TableValue.LODGERS_TABLE
TableValue.HOUSES_TABLE
TableValue.PARKINGS_TABLE
```

Для таблиц, где константы ещё не заведены, можно передавать class строкой:

```java
"tableUser"
"tableCars"
"table"
```

### Пример проверки

```java
List<String> ids = TableValue.getAllValues("tableUser", "ID");

assertTrue(ids.contains(String.valueOf(createdUserId)));
```

### Подсказки для assert

```java
assertTrue(ids.contains("1")); 
assertEquals(ids.size(), 3);
assertFalse(ids.isEmpty());
assertEquals(ids.get(0), "1");
assertTrue(ids.stream().allMatch(id -> id.matches("\\d+")));
```

---

## DB 

### Расположение

```text
src/main/java/db
```

DB-слой используется для проверки данных напрямую в базе.

В проекте используется JDBC.

---

## DbConnection

### Расположение

```text
src/main/java/db/DbConnection.java
```

`DbConnection` отвечает за подключение к БД.

Через него DB clients получают connection и выполняют SQL-запросы.

---

## UsersDbClient

### Расположение

```text
src/main/java/db/UsersDbClient.java
```

`UsersDbClient` отвечает за SQL-проверки по пользователям.

### Таблица

```text
person
```

### Методы

| Метод                      | Назначение                                           |
| -------------------------- | ---------------------------------------------------- |
| `getUserById(Integer userId)` | Получает пользователя из БД по id                    |
| `getUsersCount()`          | Получает количество пользователей в таблице `person` |

### SQL для получения пользователя

```sql
SELECT id, age, first_name, money, second_name, sex, house_id
FROM person
WHERE id = ?;
```

### Пример использования

```java
UserDbEntity userFromDb = usersDbClient.getUserById(createdUserId);

assertNotNull(userFromDb);
assertEquals(userFromDb.getFirstName(), request.getFirstName());
assertEquals(userFromDb.getSecondName(), request.getSecondName());
assertEquals(userFromDb.getAge(), request.getAge());
```

---

## Utils

### Расположение

```text
src/main/java/utils
src/test/java/utils
```

---

## PropertyReader

### Расположение

```text
src/main/java/utils/PropertyReader.java
```

Используется для чтения значений из properties-файлов.

Через него получаются:

* user;
* password;
* api token;
* DB settings.

Пример:

```java
PropertyReader.getProperty("user");
PropertyReader.getProperty("password");
PropertyReader.getProperty("api_token");
```

---

## UserTestDataFactory

### Расположение

```text
src/test/java/utils/UserTestDataFactory.java
```

Фабрика тестовых данных для пользователей.

### Основной метод

```java
UserTestDataFactory.createValidUser();
```

### Назначение

Создаёт валидный `UserCreateRequest` для тестов.

Фабрика нужна, чтобы:

* не хардкодить данные в тестах;
* генерировать разные значения;
* переиспользовать подготовку данных;
* сделать тесты чище.

Пример:

```java
UserCreateRequest request = UserTestDataFactory.createValidUser();
```

---

## Listeners

### Расположение

```text
src/test/java/listeners
```

Listeners используются для дополнительной логики во время выполнения тестов.

| Listener                | Назначение                                 |
| ----------------------- | ------------------------------------------ |
| `ScreenshotListener`    | Делает скриншоты при падении UI-тестов     |
| `TestListener`          | Логирует старт, завершение и статус тестов |
| `AnnotationTransformer` | Используется для retry-механизма           |

---

## Retry

### Расположение

```text
src/test/java/utils/Retry.java
```

Retry используется для повторного запуска нестабильных тестов.

Это помогает снизить влияние временных UI/инфраструктурных сбоев.

---

## Jenkins

### Расположение

```text
Jenkinsfile
```

Проект запускается в Jenkins.

Jenkins pipeline выполняет:

```text
Checkout проекта
→ Maven clean test
→ JUnit reports
→ Allure results
→ Allure Report
```

### Параметры Jenkins

В Jenkins можно выбрать браузер:

```text
chrome
edge
```

### Команда запуска

```bash
  mvn clean test -Dbrowser=${params.BROWSER}
```

---

## Allure Report

Allure используется для отчётности.

В отчёте отображаются:

* suites;
* features;
* stories;
* severity;
* descriptions;
* steps;
* logs;
* screenshots;
* история прогонов.

### Используемые аннотации

```java
@Epic
@Feature
@Story
@Severity
@Description
@Step
```

Пример:

```java
@Epic("PFLB Test-API")
@Feature("Users")
@Story("Create new")
@Severity(SeverityLevel.CRITICAL)
@Description("Создание пользователя через API POST /user")
@Test
public void createUserApiTest() {
    ...
}
```

---

## Log4j2

Для логирования используется Log4j2.

Пример:

```java
log.info("Start createUserApiTest");
log.info("Created user id: {}", createdUserId);
log.info("Finish createUserApiTest");
```

Логи помогают понять:

* какой тест запущен;
* какие данные созданы;
* какой id использовался;
* на каком шаге произошла ошибка.

---


## Правила Pull Request

Перед созданием PR нужно проверить:

* тесты запускаются локально;
* нет конфликтов с master;
* нет лишнего ручного логина в UI-тестах;
* нет `Thread.sleep`;
* тестовые данные удаляются после тестов;
* UI-действия вынесены в Page Object или wrappers;
* API-запросы вынесены в adapters;
* request/response модели лежат в `api/models`;
* DB-проверки вынесены в DB clients;
* Allure-аннотации заполнены;
* логи добавлены в ключевых местах.

---

## Запуск тестов локально

### Запуск всех тестов

```bash
  mvn clean test
```

### Запуск с выбором браузера

```bash
  mvn clean test -Dbrowser=chrome
```

```bash
  mvn clean test -Dbrowser=edge
```

### Запуск конкретного класса

```bash
  mvn clean test -Dtest=UsersApiTest
```

---

## Git workflow

### Создать ветку

```bash
  git checkout -b feature/название ветки
```

### Добавить изменения

```bash
  git add .
```

### Сделать commit

```bash
  git commit -m "Add users UI API DB tests"
```

### Отправить ветку

```bash
  git push origin feature/название ветки
```

### Перед PR

```bash
  mvn clean test
```

---

## Jenkins pipeline

Pipeline описан в `Jenkinsfile`.

Общий процесс:

```text
1. Jenkins забирает код из GitHub
2. Запускает mvn clean test
3. Передаёт параметр browser
4. Собирает JUnit results
5. Публикует Allure results
```

Пример команды из Jenkinsfile:

```bash
  mvn clean test -Dbrowser=${params.BROWSER}
```

---