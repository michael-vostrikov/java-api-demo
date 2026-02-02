create table app_user (
    id       serial       primary key,
    username varchar(100) not null,
    password varchar(200) not null
);


create table category
(
    id   serial       primary key,
    name varchar(100) not null
);


create table product
(
    id          serial        primary key,
    user_id     int           not null,
    category_id int           null,
    name        varchar(100)  not null,
    description varchar(2000) not null,
    status      varchar(100)  not null,
    created_at  timestamptz   not null,

    constraint product_category foreign key (category_id) references category (id),
    constraint product_user     foreign key (user_id)     references app_user (id)
);


create table product_change
(
    product_id   int   primary key,
    field_values jsonb not null,

    constraint product_change_product foreign key (product_id) references product (id)
);


create table review
(
    id           serial       primary key,
    user_id      int          not null,
    product_id   int          not null,
    status       varchar(100) not null,
    field_values json         not null,
    created_at   timestamptz  not null,
    processed_at timestamptz  null,

    constraint review_product foreign key (product_id) references product (id),
    constraint review_user    foreign key (user_id)    references app_user (id)
);
