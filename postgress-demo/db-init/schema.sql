-- Anonymous demo role
create role anon nologin;
grant usage on schema public to anon;

-- Tables
create table users(
                      id serial primary key,
                      name  text not null,
                      email text unique
);

create table surveys(
                        id serial primary key,
                        title    text not null,
                        owner_id int references users(id) on delete set null
);

create table text_questions(
                               id serial primary key,
                               survey_id int not null references surveys(id) on delete cascade,
                               prompt text not null
);

-- Seed data
insert into users (name,email) values
                                   ('Anique Ali','anique@example.com'),
                                   ('Prof Demo','prof@example.com');

insert into surveys (title,owner_id) values
                                         ('TA Feedback', 1),
                                         ('Course Exit Survey', 2);

insert into text_questions (survey_id, prompt) values
                                                   (1,'What did you like?'),
                                                   (1,'What can we improve?'),
                                                   (2,'Any comments?');

-- Grants for PostgREST demo (CRUD)
grant select, insert, update, delete on all tables in schema public to anon;
grant usage, select on all sequences in schema public to anon;
