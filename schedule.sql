CREATE SCHEMA schedule;

CREATE TABLE post (
    created_at DATETIME(6),
    member_id BIGINT NOT NULL,
    modified_at DATETIME(6),
    post_id BIGINT NOT NULL auto_increment,
    contents TEXT NOT NULL ,
    title VARCHAR(255) NOT NULL ,
    PRIMARY KEY (post_id)
)

create table member (
    created_at DATETIME(6),
    member_id BIGINT NOT NULL auto_increment,
    modified_at DATETIME(6),
    account VARCHAR(255) UNIQUE NOT NULL ,
    email VARCHAR(255) UNIQUE NOT NULL ,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (member_id)
)

create table comment (
    comment_id BIGINT NOT NULL auto_increment,
    created_at DATETIME(6),
    member_id BIGINT,
    modified_at DATETIME(6),
    post_id BIGINT,
    contents VARCHAR(255) NOT NULL,
    PRIMARY KEY (comment_id)
)

alter table comment add
    foreign key (member_id)
        references member (member_id)

alter table comment add
    foreign key (post_id)
        references post (post_id)

alter table post add
    foreign key (member_id)
        references member (member_id)
