-- schema.sql
CREATE TABLE IF NOT EXISTS locations (
    code VARCHAR(12) PRIMARY KEY,  -- Primary key, unique, not null
    city_name VARCHAR(128) NOT NULL,  -- City name, not null
    region_name VARCHAR(128) NOT NULL,  -- Region name, not null
    country_name VARCHAR(64) NOT NULL,  -- Country name, not null
    country_code VARCHAR(2) NOT NULL,  -- Country code, not null
    enabled BOOLEAN NOT NULL,  -- Enabled flag, not null
    trashed BOOLEAN NOT NULL DEFAULT false  -- Trashed flag, not null, default false
);

-- Create realtime_weather table
CREATE TABLE IF NOT EXISTS realtime_weather (
    location_code VARCHAR(12) PRIMARY KEY,
    temperature INT NOT NULL,
    humidity INT NOT NULL,
    precipitation INT NOT NULL,
    wind_speed INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    last_updated TIMESTAMP NOT NULL,

    -- Foreign key constraint
    CONSTRAINT fk_realtime_weather_location
        FOREIGN KEY (location_code)
        REFERENCES locations(code)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);