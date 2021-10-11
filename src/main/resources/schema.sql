CREATE TABLE IF NOT EXISTS png_tuber (
    id SERIAL PRIMARY KEY,
    notConnectedUrl VARCHAR(255),
    idleUrl VARCHAR(255) NOT NULL,
    speakingUrl VARCHAR(255) NOT NULL
);