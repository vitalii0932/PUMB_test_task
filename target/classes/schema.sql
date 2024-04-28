create table if not exists Type
(
    id int auto_increment primary key,
    name varchar (50) not null
);

create table if not exists Category
(
    id int auto_increment primary key ,
    name varchar(30) not null
);

insert into Category(id, name)
select (select coalesce(max(id), 0) + 1 from Category), 'First category'
where not exists(SELECT 1 from Category where name = 'First category');

insert into Category(id, name)
select (select coalesce(max(id), 0) + 1 from Category), 'Second category'
where not exists(SELECT 1 from Category where name = 'Second category');

insert into Category(id, name)
select (select coalesce(max(id), 0) + 1 from Category), 'Third category'
where not exists(SELECT 1 from Category where name = 'Third category');

insert into Category(id, name)
select (select coalesce(max(id), 0) + 1 from Category), 'Fourth category'
where not exists(SELECT 1 from Category where name = 'Fourth category');

create table if not exists Animals
(
    id int auto_increment primary key,
    type_id int references Type not null,
    sex varchar(10) not null,
    weight float not null check (weight > 0),
    cost float not null check (weight > 0),
    category_id int references Category not null
);

create table if not exists Users
(
    id int auto_increment primary key,
    email varchar(50) not null,
    password varchar(255) not null
)



