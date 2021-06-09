**Задание:**
create table tUser
             (
             UserID int IDENTITY,
             Name   varchar(50),
             Pass   varchar(50),
             Mail   varchar(255) = null
             )

create table tAudit
             (
             AuditID     int IDENTITY,
             UserID      int,
             ActionType  tinyint,  /* 1 - Registration, 2 - CheckMail, 3 - LogInPass, 4 - LogOut, 5 - LoginFail*/
             ActionDate  datetime
             )

create table tAccessToken
             (
             TokenID    int IDENTITY,
             AuditID    int,
             UserID     int,
             ExpireDate datetime
             )

Пример данных.
tUser
UserID  Name  Pass Mail
1       Petr   123 Petr@dot.com
2       Basyl  ABC XXX@YYY.com
3       Root   @#$ admin@factory.com


tAudit
AuditID   UserID  ActionType  ActionDate
1         1       1           01.11.2020 12:32:45.099
2         1       2           01.11.2020 12:35:50.439
3         2       1           14.02.2021 23:45:03.519
4         3       1           01.01.2010 14:07:10.131
5         3       2           01.01.2010 14:07:13.152
6         3       3           01.01.2010 14:05:15.167
7         3       4           01.01.2010 23:07:10.298
8         1       5           01.01.2020 12:36:15.479
9         1       3           01.01.2020 12:37:01.618


tAccessToken
TokenID   AuditID  UserID ExpireDate
1         6        3      01.01.2010 23:59:59.999
2         9        1      01.01.2020 12:52:00.000

Задания на SQL.

1. Найти всех пользователей, которые зарегистрировались, но не подтвердили почту.
2. Найти всех пользователей у которых просрочился токен доступа, текущую дату время из функции GetDate()
3. Найти всех пользователей которые зарегистрировались, но не разу не заходили в систему.
4. Найти первых четырех пользователей, которые чаще всего неверно ввродят пароль.

Задание на Java.

Спроектировать WEB сервис (Spring MVC)без View, только котролееры/репозиторий и прочие слои для сущности User.
Подключить логирование.

**Решение:**

Сервис настроен на работу с БД "diasoft". Адрес стандартный localhost:3306 логин-пароль: root, root
Заполнение БД осуществляется по средствам Flayway
Для работы с БД изспользовалось SQL2O чтобы показать работу с запросами.
В сервисе реализованно симуляция активности пользователя по средствам POST/GET запросов

Post /reg
В запросе передается body в формате JSON:
{
“name”: “string”,
“pass”: “string”,
“mail”: “string”
}
Производится добавление пользователя в БД с отметкой о его регистрации

Get /checkmail?userID=int
Происходит верификация почты пользователя с id = userID с отметкой в taudit

Get /loginpass?userID=int
Происходит login пользователя с id = userID с отметкой в taudit

Get /logout?userID=int
Происходит logaut пользователя с id = userID с отметкой в taudit

Get /loginfail?userID=int
Происходит имитация loginfail пользователя с id = userID с отметкой в taudit

Get /audit?audit=int
Выдает отчет по форме = int
1. Найти всех пользователей, которые зарегистрировались, но не подтвердили почту.
2. Найти всех пользователей у которых просрочился токен доступа, текущую дату время из функции GetDate()
3. Найти всех пользователей которые зарегистрировались, но не разу не заходили в систему.
4. Найти первых четырех пользователей, которые чаще всего неверно ввродят пароль. 

В ответ возвтрашаются список пользователей.
