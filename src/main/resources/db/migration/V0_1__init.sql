-- noinspection SqlNoDataSourceInspectionForFile

CREATE TYPE active_status AS ENUM ('active', 'temporarily_inactive', 'inactive');
CREATE TYPE song_status AS ENUM ('active', 'inactive');

-- -----------------------------------------------------
-- Table user
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_acc (
  id SERIAL NOT NULL PRIMARY KEY,
  username text NOT NULL UNIQUE,
  password text NOT NULL,
  first_name text NULL,
  last_name text NULL,
  age INT NULL,
  phone text NULL,
  email text NOT NULL UNIQUE,
  status active_status NOT NULL DEFAULT 'active',
  create_date TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL,
  update_date TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL
);

-- -----------------------------------------------------
-- Table role
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS role (
  id SERIAL NOT NULL PRIMARY KEY,
  name text NOT NULL,
  readable text NULL,
  create_date TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL,
  update_date TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL
);

INSERT INTO role (name, readable) VALUES ('user', 'Tavakasutaja');
INSERT INTO role (name, readable) VALUES ('moderator', 'Moderaator');
INSERT INTO role (name, readable) VALUES ('admin', 'Administraator');

-----------------------
-- User has role table
-----------------------

CREATE TABLE IF NOT EXISTS user_has_role (
  user_id integer REFERENCES user_acc (id) ON UPDATE CASCADE ON DELETE CASCADE,
  role_id integer REFERENCES role (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT user_has_role_pkey PRIMARY KEY (user_id, role_id)
);

-- -----------------------------------------------------
-- Table band
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS band (
  id SERIAL NOT NULL PRIMARY KEY,
  name text NOT NULL,
  introduction text NULL,
  create_date TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL,
  update_date TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL
);


-- -----------------------------------------------------
-- Table song
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS song (
  id SERIAL NOT NULL PRIMARY KEY,
  name text NOT NULL,
  suggested_band text NULL,
  content text NOT NULL,
  status song_status NOT NULL DEFAULT 'inactive',
  create_date TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL,
  update_date TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL,
  band_id integer NULL,
  user_id integer NOT NULL,
  CONSTRAINT fk_song_band
    FOREIGN KEY (band_id)
    REFERENCES band (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_song_user1
    FOREIGN KEY (user_id)
    REFERENCES user_acc (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table user likes song
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS user_likes_song (
  id SERIAL NOT NULL PRIMARY KEY,
  user_id integer NOT NULL,
  song_id integer NOT NULL,
  CONSTRAINT fk_user_likes_song_user1
    FOREIGN KEY (user_id)
    REFERENCES user_acc (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_user_likes_song_song1
    FOREIGN KEY (song_id)
    REFERENCES song (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


