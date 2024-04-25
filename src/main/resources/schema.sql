create table Type
(
    id int auto_increment primary key,
    name varchar (50) not null
);

create table Category
(
    id int auto_increment primary key ,
    name varchar(30) not null
);

create table Animals
(
    id int auto_increment primary key,
    type_id int references Type not null,
    sex varchar(10) not null,
    weight float not null check (weight > 0),
    cost float not null check (weight > 0),
    category_id int references Category not null
);

create table Users
(
    id int auto_increment primary key,
    email varchar(50) not null,
    password varchar(255) not null
)



