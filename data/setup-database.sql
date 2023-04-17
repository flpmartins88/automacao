
-- Tag Schema
CREATE SCHEMA IF NOT EXISTS tag_db;

CREATE USER IF NOT EXISTS 'tag'@'%'
    IDENTIFIED BY 'tag';

GRANT ALL
    ON `tag_db`.* TO 'tag'@'%';

--

-- Item database
CREATE SCHEMA IF NOT EXISTS item_db;

CREATE USER IF NOT EXISTS 'item'@'%'
    IDENTIFIED BY 'item';

GRANT ALL
    ON `item_db`.* TO 'item'@'%';

--

-- Order database
CREATE SCHEMA IF NOT EXISTS order_db;

CREATE USER IF NOT EXISTS 'order'@'%'
    IDENTIFIED BY 'order';

GRANT ALL
    ON `order_db`.* TO 'order'@'%';
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

CREATE TABLE IF NOT EXISTS item_db.item (
    id    BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(200) NOT NULL,
    price BIGINT       NOT NULL,
    code  VARCHAR(50)  NOT NULL
);
