##**Задание:**
create table tUser <br>
            (<br>
             UserID int IDENTITY,<br>
             Name   varchar(50),<br>
             Pass   varchar(50),<br>
             Mail   varchar(255) = null<br>
             )

create table tAudit<br>
             (<br>
             AuditID     int IDENTITY,<br>
             UserID      int,<br>
             ActionType  tinyint,  /* 1 - Registration, 2 - CheckMail, 3 - LogInPass, 4 - LogOut, 5 - LoginFail*/<br>
             ActionDate  datetime<br>
             )<br>

create table tAccessToken<br>
             (<br>
             TokenID    int IDENTITY,<br>
             AuditID    int,<br>
             UserID     int,<br>
             ExpireDate datetime<br>
             )<br>

Пример данных.<br>
tUser<br>
UserID | Name | Pass |Mail
-------|------|------|------
1       |Petr |  123 |Petr@dot.com
2       |Basyl | ABC |XXX@YYY.com
3       |Root  | @#$ |admin@factory.com


tAudit<br>
AuditID   UserID  ActionType  ActionDate<br>
1         1       1           01.11.2020 12:32:45.099<br>
2         1       2           01.11.2020 12:35:50.439<br>
3         2       1           14.02.2021 23:45:03.519<br>
4         3       1           01.01.2010 14:07:10.131<br>
5         3       2           01.01.2010 14:07:13.152<br>
6         3       3           01.01.2010 14:05:15.167<br>
7         3       4           01.01.2010 23:07:10.298<br>
8         1       5           01.01.2020 12:36:15.479<br>
9         1       3           01.01.2020 12:37:01.618<br>


tAccessToken<br>
TokenID   AuditID  UserID ExpireDate<br>
1         6        3      01.01.2010 23:59:59.999<br>
2         9        1      01.01.2020 12:52:00.000<br>

Задания на SQL.<br>

1. Найти всех пользователей, которые зарегистрировались, но не подтвердили почту.<br>
2. Найти всех пользователей у которых просрочился токен доступа, текущую дату время из функции GetDate()<br>
3. Найти всех пользователей которые зарегистрировались, но не разу не заходили в систему.<br>
4. Найти первых четырех пользователей, которые чаще всего неверно ввродят пароль.<br>

Задание на Java.

Спроектировать WEB сервис (Spring MVC)без View, только котролееры/репозиторий и прочие слои для сущности User.
Подключить логирование.

##**Решение:**

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
