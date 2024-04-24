create table Type
(
    id int auto_increment primary key,
    name varchar (50) not null
);

create table Animals
(
    id int auto_increment primary key,
    type_id int references Type,
    sex varchar(10),
    weight float,
    cost float,
    category varchar(20)
);