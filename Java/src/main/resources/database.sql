create schema if not exists ecas collate utf8_bin;
use ecas;
create table if not exists customer_ord
(
    id int auto_increment
        primary key,
    username varchar(50) default '' not null,
    password varchar(50) default '' not null,
    phone varchar(20) default '' not null,
    constraint username
        unique (username)
)
    charset=utf8;

create table if not exists express_cabinet
(
    id int auto_increment
        primary key,
    name varchar(100) default '' not null,
    address varchar(255) default '' not null,
    bigbox int(5) default 0 not null,
    middlebox int(5) default 0 not null,
    smallbox int(5) default 0 not null
)
    charset=utf8;

create table if not exists express_box
(
    id int auto_increment
        primary key,
    name varchar(20) default '' not null,
    cabinet_id int not null,
    size int(5) default 0 not null,
    is_using int(5) default 0 not null,
    constraint express_box_ibfk_1
        foreign key (cabinet_id) references express_cabinet (id)
)
    charset=utf8;

create index cabinet_id
    on express_box (cabinet_id);

create table if not exists express_company
(
    id int auto_increment
        primary key,
    company_name varchar(50) default '' not null
)
    charset=utf8;

create table if not exists courier
(
    id int auto_increment
        primary key,
    id_card varchar(30) default '' not null,
    name varchar(50) default '' not null,
    password varchar(50) default '' not null,
    phone varchar(20) default '' not null,
    company int not null,
    constraint id_card
        unique (id_card),
    constraint phone
        unique (phone),
    constraint courier_ibfk_1
        foreign key (company) references express_company (id)
)
    charset=utf8;

create table if not exists comment
(
    id int auto_increment
        primary key,
    consumer_id int null,
    object_to int null,
    content varchar(255) default '' not null,
    date datetime default CURRENT_TIMESTAMP not null,
    constraint comment_ibfk_1
        foreign key (consumer_id) references customer_ord (id),
    constraint comment_ibfk_2
        foreign key (object_to) references courier (id)
)
    charset=utf8;

create index consumer_id
    on comment (consumer_id);

create index object_to
    on comment (object_to);

create index company
    on courier (company);

create table if not exists courier_rent_box
(
    id int auto_increment
        primary key,
    courier_id int not null,
    box_id int not null,
    finish_time datetime not null,
    finish_flag int default 0 not null,
    constraint courier_rent_box_ibfk_1
        foreign key (courier_id) references courier (id),
    constraint courier_rent_box_ibfk_2
        foreign key (box_id) references express_box (id)
)
    charset=utf8;

create index box_id
    on courier_rent_box (box_id);

create index courier_id
    on courier_rent_box (courier_id);

create table if not exists courier_send_express
(
    id int auto_increment
        primary key,
    code int default 0 not null,
    track_no varchar(50) default '' null,
    courier_id int not null,
    reciver_phone varchar(20) default '' not null,
    save_time datetime default CURRENT_TIMESTAMP not null,
    box_id int not null,
    is_pick int(5) default 0 not null,
    is_delay int(5) default 0 not null,
    delay_day int(5) default 0 not null,
    constraint track_no
        unique (track_no),
    constraint courier_send_express_ibfk_1
        foreign key (courier_id) references courier (id),
    constraint courier_send_express_ibfk_2
        foreign key (box_id) references express_box (id)
)
    charset=utf8;

create index box_id
    on courier_send_express (box_id);

create index courier_id
    on courier_send_express (courier_id);

create table if not exists hibernate_sequence
(
    next_val bigint null
);

create table if not exists send_express
(
    id int auto_increment
        primary key,
    send_name varchar(50) default '' not null,
    send_phone varchar(20) default '' not null,
    send_adress varchar(255) default '' not null,
    recive_name varchar(50) default '' not null,
    recive_phone varchar(20) default '' not null,
    recive_address varchar(255) default '' not null,
    box_id int not null,
    company int null,
    detail varchar(255) default '' not null,
    constraint send_express_ibfk_1
        foreign key (box_id) references express_box (id),
    constraint send_express_ibfk_2
        foreign key (company) references express_company (id)
)
    charset=utf8;

create table if not exists express_send_order
(
    id int auto_increment
        primary key,
    courier_id int null,
    customer_id int not null,
    express_id int not null,
    order_no varchar(32) default '' not null,
    payment decimal(20,2) default 0.00 not null,
    status int(5) default 0 not null,
    code int(5) default 0 null,
    is_save int(5) default 0 not null,
    payment_time datetime null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    constraint order_no
        unique (order_no),
    constraint express_send_order_ibfk_1
        foreign key (courier_id) references courier (id),
    constraint express_send_order_ibfk_2
        foreign key (express_id) references send_express (id),
    constraint express_send_order_ibfk_3
        foreign key (customer_id) references customer_ord (id)
)
    charset=utf8;

create index courier_id
    on express_send_order (courier_id);

create index customer_id
    on express_send_order (customer_id);

create index express_id
    on express_send_order (express_id);

create index box_id
    on send_express (box_id);

create index company
    on send_express (company);

INSERT INTO ecas.express_cabinet (id, name, address, bigbox, middlebox, smallbox) VALUES (1, '蜂巢1号柜', '湖南省衡阳市珠晖区解放路', 10, 10, 8);
INSERT INTO ecas.express_cabinet (id, name, address, bigbox, middlebox, smallbox) VALUES (2, '蜂巢2号柜', '湖南省衡阳市珠晖区解放路', 10, 10, 8);
INSERT INTO ecas.express_cabinet (id, name, address, bigbox, middlebox, smallbox) VALUES (3, '蜂巢3号柜', '湖南省衡阳市珠晖区解放路', 10, 10, 8);
INSERT INTO ecas.express_cabinet (id, name, address, bigbox, middlebox, smallbox) VALUES (4, '蜂巢8号柜', '湖南省常德市鼎城区解放西路', 10, 10, 8);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (1, '1-01', 1, 0, 1);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (2, '1-02', 1, 1, 1);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (3, '1-03', 1, 0, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (4, '1-04', 1, 2, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (5, '1-05', 1, 1, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (6, '2-01', 2, 0, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (7, '2-02', 2, 1, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (8, '2-03', 2, 0, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (9, '2-04', 2, 2, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (10, '2-05', 2, 1, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (11, '3-01', 3, 0, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (12, '3-02', 3, 1, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (13, '3-03', 3, 0, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (14, '3-04', 3, 2, 0);
INSERT INTO ecas.express_box (id, name, cabinet_id, size, is_using) VALUES (15, '3-05', 3, 1, 0);

INSERT INTO ecas.hibernate_sequence values(1);