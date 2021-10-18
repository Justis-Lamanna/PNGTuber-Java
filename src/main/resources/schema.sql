CREATE TABLE IF NOT EXISTS png_tuber (
    id SERIAL PRIMARY KEY,
    notConnectedUrl VARCHAR(255),
    idleUrl VARCHAR(255) NOT NULL,
    speakingUrl VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cdn_image (
    id SERIAL PRIMARY KEY,
    mime_type VARCHAR(25) NOT NULL,
    image BLOB NOT NULL
);