create database if not exists ecas;
use ecas;


create table if not exists customer_ord
(
    id       int(11) primary key auto_increment,
    username varchar(50) unique default '' not null,
    password varchar(50)        default '' not null,
    phone    varchar(20)        default '' not null
) engine = innodb
  charset = utf8;

# insert into customer_ord value (1, 'nieaowei', 'nieaowei', '123');

create table if not exists courier
(
    id       int(11) primary key auto_increment,
    id_card  varchar(30) default '' unique not null,
    name     varchar(50) default ''        not null,
    password varchar(50) default ''        not null,
    phone    varchar(20) default '' unique not null,
    company  int(11)                       not null,
    foreign key (company) references express_company (id)
) engine = innodb
  charset = utf8;


create table if not exists express_cabinet
(
    id        int(11) primary key auto_increment,
    name      varchar(100)     not null default '',
    address   varchar(255)     not null default '',
    bigbox    int(5) default 0 not null,
    middlebox int(5) default 0 not null,
    smallbox  int(5) default 0 not null
) engine = innodb
  charset = utf8;


create table if not exists express_box
(
    id         int(11) primary key auto_increment,
    name       varchar(20) default '' not null,
    cabinet_id int(11)                not null,
    size       int(5)      default 0  not null,
    is_using   int(5)      default 0  not null,
    foreign key (cabinet_id) references express_cabinet (id)
) engine = innodb
  charset = utf8;

create table if not exists express_company
(
    id           int(11) primary key auto_increment,
    company_name varchar(50) default '' not null
) engine = innodb
  charset = utf8;

create table if not exists courier_rent_box
(
    id          int(11) primary key auto_increment,
    courier_id  int(11)  not null,
    box_id      int(11)  not null,
    finish_time datetime not null,
    finish_flag int      not null default 0,
    foreign key (courier_id) references courier (id),
    foreign key (box_id) references express_box (id)
) engine = innodb
  charset = utf8;


create table if not exists courier_send_express
(
    id            int(11) primary key auto_increment,
    code          int(11)     not null default 0,
    track_no      varchar(50) unique   default '',
    courier_id    int(11)     not null,
    reciver_phone varchar(20) not null default '',
    save_time     datetime             default current_timestamp not null,
    box_id        int(11)     not null,
    is_pick       int(5)      not null default 0,
    is_delay      int(5)               default 0 not null,
    delay_day     int(5)               default 0 not null,
    foreign key (courier_id) references courier (id),
    foreign key (box_id) references express_box (id)
) engine = innodb
  charset = utf8;

drop table if exists express_send_order;
create table if not exists express_send_order
(
    id           int(11) primary key auto_increment,
    courier_id   int(11),
    customer_id  int(11)                                  not null,
    express_id   int(11)                                  not null,
    order_no     varchar(32)    default '' unique         not null,
    payment      decimal(20, 2) default 0                 not null,
    status       int(5)         default 0                 not null,
    payment_time datetime,
    create_time  datetime       default current_timestamp not null,
    foreign key (courier_id) references courier (id),
    foreign key (express_id) references send_express (id),
    foreign key (customer_id) references customer_ord (id)
) engine = innodb
  charset = utf8;

drop table if exists send_express;
create table if not exists send_express
(
    id             int(11) primary key auto_increment,
    send_name      varchar(50)  default '' not null,
    send_phone     varchar(20)  default '' not null,
    send_adress    varchar(255) default '' not null,
    recive_name    varchar(50)  default '' not null,
    recive_phone   varchar(20)  default '' not null,
    recive_address varchar(255) default '' not null,
    box_id         int(11)                 not null,
    company        int(11),
    is_save        int(5)       default 0  not null,
    detail         varchar(255) default '' not null,
    foreign key (box_id) references express_box (id),
    foreign key (company) references express_company (id)
) engine = innodb
  charset = utf8;


create table if not exists comment
(
    id          int(11) primary key auto_increment,
    consumer_id int(11),
    object_to   int(11),
    content     varchar(255) default ''                not null,
    date        datetime     default current_timestamp not null,
    foreign key (consumer_id) references customer_ord (id),
    foreign key (object_to) references courier (id)
) engine = innodb
  charset = utf8;

# 快递公司数据
insert into express_company (company_name)
values ('圆通快递');
insert into express_company (company_name)
values ('申通快递');
insert into express_company (company_name)
values ('百世快递');
insert into express_company (company_name)
values ('顺丰快递');
insert into express_company (company_name)
values ('韵达快递');
insert into express_company (company_name)
values ('疾风速递');

# 快递柜

insert into express_cabinet (name, address, bigbox, middlebox, smallbox)
values ('蜂巢1号柜', '湖南省衡阳市珠晖区解放路', 10, 10, 8);
insert into express_cabinet (name, address, bigbox, middlebox, smallbox)
values ('蜂巢2号柜', '湖南省衡阳市珠晖区解放路', 10, 10, 8);
insert into express_cabinet (name, address, bigbox, middlebox, smallbox)
values ('蜂巢3号柜', '湖南省衡阳市珠晖区解放路', 10, 10, 8);
insert into express_cabinet (name, address, bigbox, middlebox, smallbox)
values ('蜂巢8号柜', '湖南省常德市鼎城区解放西路', 10, 10, 8);


# 快递箱

insert into express_box (name, cabinet_id, size, is_using)
VALUES ('1-01', 1, 0, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('1-02', 1, 1, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('1-03', 1, 0, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('1-04', 1, 2, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('1-05', 1, 1, 0);


insert into express_box (name, cabinet_id, size, is_using)
VALUES ('2-01', 2, 0, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('2-02', 2, 1, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('2-03', 2, 0, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('2-04', 2, 2, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('2-05', 2, 1, 0);


insert into express_box (name, cabinet_id, size, is_using)
VALUES ('3-01', 3, 0, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('3-02', 3, 1, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('3-03', 3, 0, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('3-04', 3, 2, 0);
insert into express_box (name, cabinet_id, size, is_using)
VALUES ('3-05', 3, 1, 0);

insert into courier_send_express (code, track_no, courier_id, reciver_phone, save_time, box_id, is_pick, is_delay,
                                  delay_day)
VALUES (1234, uuid_short(), 1, '13211223434', '2020-09-09', '1', 0, 0, 0);


select *
from express_cabinet
where address like '%解放%';


select uuid_short();

