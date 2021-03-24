CREATE TABLE Marker(id SERIAL PRIMARY KEY,
        name VARCHAR(100),
        latitude DOUBLE PRECISION,
        longitude DOUBLE PRECISION);

CREATE TABLE Message(id SERIAL PRIMARY KEY,
    message TEXT,
    marker INT,
    username TEXT,
    timestamp TIMESTAMP,
    deleted BOOLEAN,
    CONSTRAINT fk_marker FOREIGN KEY(marker) REFERENCES marker(id)
);