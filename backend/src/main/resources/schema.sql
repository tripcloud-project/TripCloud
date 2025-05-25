DROP DATABASE IF EXISTS tripcloud;
create database tripcloud;

use tripcloud;

CREATE TABLE `file` (
	`file_id`	BIGINT	NOT NULL,
	`member_id`	BIGINT	NOT NULL,
	`s3_key`	VARCHAR(768)	NOT NULL	COMMENT 'UNIQUE',
	`original_filename`	VARCHAR(255)	NOT NULL,
	`size`	BIGINT	NOT NULL,
	`content_type`	VARCHAR(255)	NOT NULL	DEFAULT "",
	`description`	VARCHAR(255)	NOT NULL	DEFAULT "",
	`latitude`	VARCHAR(255)	NULL,
	`longitude`	VARCHAR(255)	NULL,
	`sido`	VARCHAR(255)	NULL,
	`sigungu`	VARCHAR(255)	NULL,
	`eupmyeondong`	VARCHAR(255)	NULL,
	`taken_at`	DATETIME	NULL	DEFAULT NOW(),
	`created_at`	DATETIME	NOT NULL	DEFAULT NOW(),
	`modified_at`	DATETIME	NOT NULL	DEFAULT NOW(),
	`is_deleted`	BOOLEAN	NOT NULL	DEFAULT 0	COMMENT 'ON UPDATE CASCADE?',
	`deleted_at`	DATETIME	NULL	DEFAULT NOW()	COMMENT '30일 이후 영구삭제'
);

CREATE TABLE `member` (
	`member_id`	BIGINT	NOT NULL,
	`email`	VARCHAR(255)	NOT NULL	COMMENT 'UNIQUE',
	`password`	VARCHAR(255)	NOT NULL,
	`role`	ENUM('USER', 'ADMIN')	NOT NULL	DEFAULT 'USER',
	`name`	VARCHAR(255)	NOT NULL,
	`profile_image`	VARCHAR(512)	NULL,
	`used_storage`	BIGINT 	NOT NULL	DEFAULT 0,
	`created_at`	DATETIME	NOT NULL	DEFAULT NOW(),
	`modified_at`	DATETIME	NOT NULL	DEFAULT NOW(),
	`is_deleted`	BOOLEAN	NOT NULL	DEFAULT 0,
	`main_badge_id`	BIGINT	NULL,
	`max_storage`	BIGINT 	NOT NULL DEFAULT 32212254720
);

CREATE TABLE `post_comment` (
	`post_comment_id`	BIGINT	NOT NULL,
	`member_id`	BIGINT	NOT NULL,
	`post_id`	BIGINT	NOT NULL,
	`content`	TEXT	NOT NULL,
    `created_at`	DATETIME	NOT NULL	DEFAULT NOW()
);

CREATE TABLE `badge` (
	`badge_id`	BIGINT	NOT NULL,
	`name`	VARCHAR(255)	NOT NULL,
	`description`	VARCHAR(255)	NOT NULL,
	`image`	VARCHAR(512)	NOT NULL
);

CREATE TABLE `hashtag` (
	`hashtag_id`	BIGINT	NOT NULL,
	`keyword`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `file_hashtag` (
	`file_id`	BIGINT	NOT NULL,
	`hashtag_id`	BIGINT	NOT NULL
);

CREATE TABLE `post_like` (
	`member_id`	BIGINT	NOT NULL,
	`post_id`	BIGINT	NOT NULL
);

CREATE TABLE `thumbnail` (
	`thumbnail_id`	BIGINT	NOT NULL	COMMENT 'PK',
	`member_id`	BIGINT	NOT NULL	COMMENT 'FK',
	`photo_id`	BIGINT	NOT NULL,
	`sido`	VARCHAR(255)	NOT NULL	DEFAULT "",
	`sigungu`	VARCHAR(255)	DEFAULT ""
);

CREATE TABLE `post` (
	`post_id`	BIGINT	NOT NULL,
	`member_id`	BIGINT	NOT NULL,
	`title`	VARCHAR(255)	NOT NULL,
	`content`	TEXT	NOT NULL,
	`file_path`	VARCHAR(512)	NULL,
	`created_at`	DATETIME	NOT NULL	DEFAULT NOW(),
	`modified_at`	DATETIME	NOT NULL	DEFAULT NOW()
);

CREATE TABLE `attraction` (
	`attraction_id`	BIGINT	NOT NULL,
	`attraction_category_id`	BIGINT	NOT NULL,
	`name`	VARCHAR(255)	NOT NULL,
	`description`	VARCHAR(255)	NOT NULL,
	`latitude`	DOUBLE	NOT NULL,
	`longitude`	DOUBLE	NOT NULL,
	`image`	VARCHAR(512)	NULL
);

CREATE TABLE `attraction_category` (
	`attraction_category_id`	BIGINT	NOT NULL,
	`name`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `member_activity` (
	`activity_id`	BIGINT	NOT NULL,
	`member_id`	BIGINT	NOT NULL,
	`activity`	VARCHAR(255)	NOT NULL,
	`created_at`	DATETIME	NOT NULL	DEFAULT NOW()
);

CREATE TABLE `member_badge` (
	`badge_id`	BIGINT	NOT NULL,
	`member_id`	BIGINT	NOT NULL
);

ALTER TABLE `file` ADD CONSTRAINT `PK_FILE` PRIMARY KEY (
	`file_id`
);

ALTER TABLE `member` ADD CONSTRAINT `PK_MEMBER` PRIMARY KEY (
	`member_id`
);

ALTER TABLE `post_comment` ADD CONSTRAINT `PK_POST_COMMENT` PRIMARY KEY (
	`post_comment_id`
);

ALTER TABLE `badge` ADD CONSTRAINT `PK_BADGE` PRIMARY KEY (
	`badge_id`
);

ALTER TABLE `hashtag` ADD CONSTRAINT `PK_HASHTAG` PRIMARY KEY (
	`hashtag_id`
);

ALTER TABLE `thumbnail` ADD CONSTRAINT `PK_THUMBNAIL` PRIMARY KEY (
	`thumbnail_id`
);

ALTER TABLE `post` ADD CONSTRAINT `PK_POST` PRIMARY KEY (
	`post_id`
);

ALTER TABLE `attraction` ADD CONSTRAINT `PK_ATTRACTION` PRIMARY KEY (
	`attraction_id`
);

ALTER TABLE `attraction_category` ADD CONSTRAINT `PK_ATTRACTION_CATEGORY` PRIMARY KEY (
	`attraction_category_id`
);

ALTER TABLE `member_activity` ADD CONSTRAINT `PK_MEMBER_ACTIVITY` PRIMARY KEY (
	`activity_id`
);



-- AUTO_INCREMENT 추가
ALTER TABLE file
MODIFY COLUMN file_id BIGINT AUTO_INCREMENT;

ALTER TABLE member
MODIFY COLUMN member_id BIGINT AUTO_INCREMENT;

ALTER TABLE hashtag
MODIFY COLUMN hashtag_id BIGINT AUTO_INCREMENT;

ALTER TABLE badge
MODIFY COLUMN badge_id BIGINT AUTO_INCREMENT;

ALTER TABLE post
MODIFY COLUMN post_id BIGINT AUTO_INCREMENT;

ALTER TABLE post_comment
MODIFY COLUMN post_comment_id BIGINT AUTO_INCREMENT;

ALTER TABLE member_activity
MODIFY COLUMN activity_id BIGINT AUTO_INCREMENT;

ALTER TABLE attraction_category
MODIFY COLUMN attraction_category_id BIGINT AUTO_INCREMENT;

ALTER TABLE attraction
MODIFY COLUMN attraction_id BIGINT AUTO_INCREMENT;

ALTER TABLE thumbnail
MODIFY COLUMN thumbnail_id BIGINT AUTO_INCREMENT;

ALTER TABLE thumbnail 
ADD CONSTRAINT unique_photo_region UNIQUE(member_id, sido, sigungu);

-- UNIQUE 추가
ALTER TABLE member
ADD CONSTRAINT uq_member_email UNIQUE (email);

ALTER TABLE file
ADD CONSTRAINT uq_file_s3key UNIQUE (s3_key);