DROP TABLE IF EXISTS licence;

CREATE TABLE licence (
  licence_id        BIGINT PRIMARY KEY NOT NULL,
  organization_id   BIGINT NOT NULL,
  licence_type      VARCHAR(20) NOT NULL,
  product_name      VARCHAR(100) NOT NULL,
  licence_max       INT  NOT NULL,
  licence_allocated INT,
  comment           VARCHAR(100));
