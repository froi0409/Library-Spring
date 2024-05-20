create database library
    with owner postgres;

create type public.user_role as enum ('LIBRARIAN', 'STUDENT', 'ADMINISTRATOR');

alter type public.user_role owner to postgres;

create type public.student_status as enum ('ACTIVE', 'INACTIVE');

alter type public.student_status owner to postgres;

create type public.book_reservation_status as enum ('SERVED', 'NO_SERVED');

alter type public.book_reservation_status owner to postgres;

create type public.book_loan_status as enum ('IN_TIME', 'OUT_OF_TIME', 'RETURNED_IN_TIME', 'RETURNED_OUT_OF_TIME');

alter type public.book_loan_status owner to postgres;

create table public.degree
(
    id   serial
        constraint degree_pk
            primary key,
    name varchar(100) not null
);

alter table public.degree
    owner to postgres;

create table public.book
(
    code         varchar(20)                  not null
        constraint book_pk
            primary key,
    title        varchar(100)                 not null,
    publish_date date,
    publisher    varchar(100)                 not null,
    author       varchar(100)                 not null,
    cost         numeric(8, 2) default 100.00 not null,
    stock        integer                      not null
);

alter table public.book
    owner to postgres;

create table public.student
(
    id         varchar(15)  not null
        constraint student_pk
            primary key,
    first_name varchar(100) not null,
    last_name  varchar(100) not null,
    degree     integer      not null
        constraint student_degree_id_fk
            references public.degree,
    birth_date date,
    status     varchar(50) default 'ACTIVE'::character varying,
    email      varchar(200)
);

alter table public.student
    owner to postgres;

create table public.book_loan
(
    book          varchar(20)                                      not null
        constraint book_loan_book_code_fk
            references public.book,
    student       varchar(15)                                      not null
        constraint book_loan_student_id_fk
            references public.student,
    loan_date     date                                             not null,
    id            serial
        constraint book_loan_pk
            primary key,
    loan_total    numeric(8, 2),
    delay_total   numeric(8, 2),
    returned_date date,
    status        varchar(50) default 'IN_TIME'::character varying not null
);

alter table public.book_loan
    owner to postgres;

create table public."user"
(
    username         varchar(50)                                      not null
        constraint user_pk
            primary key,
    password         varchar(200)                                     not null,
    role             varchar(50) default 'STUDENT'::character varying not null,
    student          varchar(15)
        constraint user_student_id_fk
            references public.student,
    token_expiration timestamp
);

alter table public."user"
    owner to postgres;

create table public.reservation
(
    id                    serial
        constraint reservation_pk
            primary key,
    student               varchar(15)                                        not null
        constraint reservation_student_id_fk
            references public.student,
    book                  varchar(20)                                        not null
        constraint reservation_book_code_fk
            references public.book,
    status                varchar(50) default 'NO_SERVED'::character varying not null,
    reservation_date      date        default now()                          not null,
    reservation_validated date
);

alter table public.reservation
    owner to postgres;

