-- CDN
CREATE SCHEMA CDN;

DROP TABLE IF EXISTS CDN.ASSET;

CREATE TABLE CDN.ASSET (
    ID UUID NOT NULL,
    FILE_NAME VARCHAR(512) NOT NULL,
    CONTENT_TYPE VARCHAR(256) NOT NULL,
    CONTENT_HASH VARCHAR(512) NOT NULL,
    FILE_SIZE BIGINT NOT NULL,
    UPLOAD_TIME TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

ALTER TABLE CDN.ASSET ADD CONSTRAINT PK_ASSET PRIMARY KEY(ID);

CREATE INDEX FILE_NAME_INDEX ON CDN.ASSET (FILE_NAME);
CREATE INDEX CONTENT_HASH_INDEX ON CDN.ASSET (CONTENT_HASH);

-- USER_MANAGEMENT
CREATE SCHEMA USER_MANAGEMENT;

CREATE TABLE USER_MANAGEMENT.USER_PROFILE (
    ID UUID NOT NULL,
    FIRST_NAME VARCHAR(512) NOT NULL,
    LAST_NAME VARCHAR(512) NOT NULL,
    EMAIL VARCHAR(512) NOT NULL,
    CREATED_AT TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    UPDATED_AT TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    PROFILE_PICTURE_URL VARCHAR(2048),
    AUTHENTICATION_PROVIDER_ID VARCHAR(512) UNIQUE NOT NULL,
    FOLLOWERS_COUNT BIGINT NOT NULL DEFAULT 0,
    FOLLOWED_COUNT BIGINT NOT NULL DEFAULT 0
);

ALTER TABLE USER_MANAGEMENT.USER_PROFILE ADD CONSTRAINT PK_USER_PROFILE PRIMARY KEY(ID);

CREATE INDEX AUTHENTICATION_PROVIDER_ID_INDEX ON USER_MANAGEMENT.USER_PROFILE (AUTHENTICATION_PROVIDER_ID);


CREATE TABLE USER_MANAGEMENT.USER_FOLLOW (
    ID UUID NOT NULL,
    FOLLOWER_ID UUID NOT NULL,
    FOLLOWED_ID UUID NOT NULL,
    CREATED_AT TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

ALTER TABLE USER_MANAGEMENT.USER_FOLLOW ADD CONSTRAINT PK_USER_FOLLOW PRIMARY KEY(ID);

ALTER TABLE USER_MANAGEMENT.USER_FOLLOW ADD CONSTRAINT FK_FOLLOWER FOREIGN KEY(FOLLOWER_ID) REFERENCES USER_MANAGEMENT.USER_PROFILE(ID);
ALTER TABLE USER_MANAGEMENT.USER_FOLLOW ADD CONSTRAINT FK_FOLLOWED FOREIGN KEY(FOLLOWED_ID) REFERENCES USER_MANAGEMENT.USER_PROFILE(ID);
ALTER TABLE USER_MANAGEMENT.USER_FOLLOW ADD CONSTRAINT FOLLOWER_FOLLOWED UNIQUE(FOLLOWER_ID, FOLLOWED_ID);

CREATE INDEX FOLLOWER_ID_INDEX ON USER_MANAGEMENT.USER_FOLLOW (FOLLOWER_ID);
CREATE INDEX FOLLOWED_ID_INDEX ON USER_MANAGEMENT.USER_FOLLOW (FOLLOWED_ID);