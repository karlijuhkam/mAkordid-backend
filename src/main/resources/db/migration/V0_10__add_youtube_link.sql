ALTER TABLE song ADD COLUMN youtube_url TEXT;

UPDATE song SET youtube_url = 'https://www.youtube.com/watch?v=cxKL0y-Jj0g' WHERE id = 1;
