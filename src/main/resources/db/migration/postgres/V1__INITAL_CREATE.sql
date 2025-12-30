CREATE TABLE users (
  id           BIGINT PRIMARY KEY                    NOT NULL,
  -- created_date TIMESTAMP WITH TIME ZONE              NOT NULL,
  -- updated_date TIMESTAMP WITH TIME ZONE              NOT NULL,
  -- created_by   CHARACTER VARYING(64)                 NOT NULL,
  -- updated_by   CHARACTER VARYING(64)                 NOT NULL,
  --
  username     CHARACTER VARYING(64)                 NOT NULL,
  password_hash     CHARACTER VARYING(1024)               NOT NULL,
  first_name     CHARACTER VARYING(64)                 ,
  last_name     CHARACTER VARYING(64)
);
