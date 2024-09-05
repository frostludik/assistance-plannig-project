INSERT INTO CUSTOMER (IS_ACTIVE, SHEET_CUSTOMER_ID)
VALUES (1, 'Client1'),
       (1, 'Client2'),
       (1, 'Client3');

INSERT INTO WEEK (WEEK_DAY)
values ('Pondělí'),
       ('Úterý'),
       ('Středa'),
       ('Čtvrtek'),
       ('Pátek'),
       ('Sobota'),
       ('Neděle');


INSERT INTO PREDEFINED_MEETING
(END_TIME, START_TIME, USER_ID, VALID_FROM, VALID_UNTIL, WEEK_ID)
values ('16:00', '15:00', 1, '2024-07-01', null, 1),
       ('13:00', '12:00', 1, '2024-07-01', '2024-08-01', 1),
       ('11:00', '10:00', 1, '2024-01-01', '2024-02-01', 1);

INSERT INTO PREDEFINED_MEETING
(END_TIME, START_TIME, USER_ID, VALID_FROM, VALID_UNTIL, WEEK_ID)
values ('16:00', '15:00', 2, '2024-07-01', null, 1),
       ('13:00', '12:00', 2, '2024-07-01', '2024-08-01', 1),
       ('11:00', '10:00', 2, '2024-01-01', '2024-02-01', 1);

INSERT INTO PREDEFINED_MEETING
(END_TIME, START_TIME, USER_ID, VALID_FROM, VALID_UNTIL, WEEK_ID)
values ('16:00', '15:00', 3, '2024-07-01', null, 1),
       ('13:00', '12:00', 3, '2024-07-01', '2024-08-01', 1),
       ('11:00', '10:00', 3, '2024-01-01', '2024-02-01', 1);

INSERT INTO PREDEFINED_MEETING
(END_TIME, START_TIME, USER_ID, VALID_FROM, VALID_UNTIL, WEEK_ID)
values ('16:00', '15:00', 1, '2024-07-01', null, 2),
       ('13:00', '12:00', 1, '2024-07-01', '2024-08-01', 2),
       ('11:00', '10:00', 1, '2024-01-01', '2024-02-01', 2);