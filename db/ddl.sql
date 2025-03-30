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