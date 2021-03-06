CREATE TABLE IF NOT EXISTS png_tuber (
    id SERIAL PRIMARY KEY,
    snowflake VARCHAR(18) NOT NULL,
    variant VARCHAR(30),
    not_connected_url VARCHAR(255),
    idle_url VARCHAR(255) NOT NULL,
    speaking_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS cdn_image (
    id SERIAL PRIMARY KEY,
    mime_type VARCHAR(25) NOT NULL,
    image BLOB NOT NULL
);