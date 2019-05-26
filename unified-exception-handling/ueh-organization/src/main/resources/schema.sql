DROP TABLE IF EXISTS organization;

CREATE TABLE organization (
  id        BIGINT PRIMARY KEY NOT NULL,
  name                   VARCHAR(100) NOT NULL,
  contact_name           VARCHAR(100) NOT NULL,
  contact_email          VARCHAR(50) NOT NULL,
  contact_phone          VARCHAR(15) NOT NULL);

