ALTER TABLE user_likes_song ADD COLUMN create_time TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL;
ALTER TABLE user_likes_song ADD COLUMN update_time TIMESTAMP without TIME ZONE DEFAULT now() NOT NULL;
