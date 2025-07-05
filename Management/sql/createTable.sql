create table student_info
(
    id          int primary key auto_increment comment 'ID',
    stu_id      varchar(10)       not null unique comment '学号',
    stu_name    varchar(10)       not null comment '姓名',
    stu_gender  enum ('男', '女') not null comment '性别',
    stu_birth   date              not null comment '出生日期',
    stu_pwd     varchar(15)       not null comment '密码',
    cla_id      int               not null comment '班级ID',
    create_time timestamp default current_timestamp comment '创建时间(无需指定)',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间(无需指定)',
    foreign key (cla_id) references class_info (id) on delete cascade
) comment '学生信息表';

create table class_info
(
    id          int primary key auto_increment comment 'ID',
    cla_name    varchar(15) not null unique comment '班级名',
    cla_start   date        not null comment '入学时间',
    cla_end     date        not null comment '毕业时间',
    create_time timestamp default current_timestamp comment '创建时间(无需指定)',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间(无需指定)'
) comment '班级信息表';

create table course_info
(
    id          int primary key auto_increment comment 'ID',
    cou_name    varchar(15)      not null unique comment '课程名',
    cou_major   varchar(15)      not null comment '所属专业',
    cou_cre     tinyint unsigned not null comment '学分',
    create_time timestamp default current_timestamp comment '创建时间(无需指定)',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间(无需指定)'
) comment '课程信息表';

create table teacher_info
(
    id          int primary key auto_increment comment 'ID',
    tea_name    varchar(10)       not null comment '姓名',
    tea_gender  enum ('男', '女') not null comment '性别',
    tea_pwd     varchar(15)       not null comment '密码',
    create_time timestamp default current_timestamp comment '创建时间(无需指定)',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间(无需指定)'
) comment '教师信息表';

create table tea_cou_info
(
    id          int primary key auto_increment comment 'ID',
    tea_id      int not null comment '教师ID',
    cou_id      int not null comment '课程ID',
    create_time timestamp default current_timestamp comment '创建时间(无需指定)',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间(无需指定)',
    foreign key (tea_id) references teacher_info (id) on delete cascade,
    foreign key (cou_id) references course_info (id) on delete cascade,
    unique (tea_id, cou_id)
) comment '教师-课程信息表';

create table teach_info
(
    id          int primary key auto_increment comment 'ID',
    tea_id      int  not null comment '教师ID',
    cou_id      int  not null comment '课程ID',
    teach_start date not null comment '开课时间',
    teach_end   date not null comment '结课时间',
    create_time timestamp default current_timestamp comment '创建时间(无需指定)',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间(无需指定)',
    foreign key (tea_id, cou_id) references  tea_cou_info(tea_id, cou_id) on delete cascade
) comment '授课信息表';

create table grade_info
(
    id          int primary key auto_increment comment 'ID',
    teach_id    int not null comment '授课ID',
    stu_id      int not null comment '学生ID',
    g_score     tinyint unsigned comment '分数(可以为空，表示未赋分，此时仅代表选课)',
    create_time timestamp default current_timestamp comment '创建时间(无需指定)',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间(无需指定)',
    foreign key (teach_id) references teach_info (id) on delete cascade,
    foreign key (stu_id) references student_info (id) on delete cascade,
    unique key (teach_id, stu_id)
) comment '成绩信息表';

create table admin_info
(
    id          int primary key auto_increment comment 'ID',
    ad_uname    varchar(10) not null unique comment '用户名',
    ad_pwd      varchar(15) not null comment '密码',
    create_time timestamp default current_timestamp comment '创建时间(无需指定)',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间(无需指定)'
) comment '管理员信息表';