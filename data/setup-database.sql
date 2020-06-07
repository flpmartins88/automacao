
-- Tag Schema
CREATE SCHEMA IF NOT EXISTS tag_db;

CREATE USER IF NOT EXISTS 'tag'@'%'
    IDENTIFIED BY 'tag';

GRANT ALL
    ON `tag_db`.* TO 'tag'@'%';

--

FLUSH PRIVILEGES;