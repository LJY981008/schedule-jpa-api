CREATE SCHEMA schedule;

CREATE TABLE post (
    created_at DATETIME(6),
    member_id BIGINT NOT NULL,
    modified_at DATETIME(6),
    post_id BIGINT NOT NULL AUTO_INCREMENT,
    contents TEXT NOT NULL ,
    title VARCHAR(255) NOT NULL ,
    PRIMARY KEY (post_id)
)

CREATE TABLE member (
    created_at DATETIME(6),
    member_id BIGINT NOT NULL AUTO_INCREMENT,
    modified_at DATETIME(6),
    account VARCHAR(255) UNIQUE NOT NULL ,
    email VARCHAR(255) UNIQUE NOT NULL ,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (member_id)
)

CREATE TABLE comment (
    comment_id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    member_id BIGINT NOT NULL,
    modified_at DATETIME(6),
    post_id BIGINT NOT NULL,
    contents VARCHAR(255) NOT NULL,
    PRIMARY KEY (comment_id)
)

ALTER TABLE comment ADD
    foreign key (member_id)
        references member (member_id)

alter table comment add
    foreign key (post_id)
        references post (post_id)

alter table post add
    foreign key (member_id)
        references member (member_id)
