
-- Tag Schema
CREATE SCHEMA IF NOT EXISTS tag_db;

CREATE USER IF NOT EXISTS 'tag'@'%'
    IDENTIFIED BY 'tag';

GRANT ALL
    ON `tag_db`.* TO 'tag'@'%';

--

FLUSH PRIVILEGES;

# CREATE TABLE tag_db.tag (
#     id        INTEGER      PRIMARY KEY AUTO_INCREMENT,
#     quantity  INTEGER      NOT NULL,
#     tag_group VARCHAR(50),
#
#     created   TIMESTAMP    NOT NULL,
#     produced  TIMESTAMP    NOT NULL,
#     analyzed  TIMESTAMP    NOT NULL,
#     shipped   TIMESTAMP    NOT NULL,
#     canceled  TIMESTAMP    NOT NULL,
#
#     item_id   VARCHAR(50)  NOT NULL,
#     item_name VARCHAR(200) NOT NULL
# );
