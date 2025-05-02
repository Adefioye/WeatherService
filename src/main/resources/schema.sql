-- Schema for KokoSky Weather Service

-- Drop tables if they exist (in reverse order to avoid constraint violations)
DROP TABLE IF EXISTS weather_hourly;
DROP TABLE IF EXISTS realtime_weather;
DROP TABLE IF EXISTS locations;

-- Create the locations table
CREATE TABLE locations (
    code VARCHAR(12) PRIMARY KEY,
    city_name VARCHAR(128) NOT NULL,
    region_name VARCHAR(128) NOT NULL,
    country_name VARCHAR(64) NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    enabled BOOLEAN NOT NULL,
    trashed BOOLEAN NOT NULL DEFAULT FALSE
);

-- Create the realtime_weather table with foreign key to locations
CREATE TABLE realtime_weather (
    location_code VARCHAR(12) PRIMARY KEY,
    temperature INT NOT NULL,
    humidity INT NOT NULL,
    precipitation INT NOT NULL,
    wind_speed INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    last_updated TIMESTAMP,
    CONSTRAINT fk_realtime_weather_location FOREIGN KEY (location_code)
        REFERENCES locations (code) ON DELETE CASCADE
);

-- Create the weather_hourly table with composite primary key
CREATE TABLE weather_hourly (
    location_code VARCHAR(12) NOT NULL,
    hour_of_day INT NOT NULL,
    temperature INT NOT NULL,
    precipitation INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    PRIMARY KEY (location_code, hour_of_day),
    CONSTRAINT fk_hourly_weather_location FOREIGN KEY (location_code)
        REFERENCES locations (code) ON DELETE CASCADE
);

-- Add indexes for improved query performance
CREATE INDEX idx_locations_country_code ON locations (country_code);
CREATE INDEX idx_locations_enabled ON locations (enabled);
CREATE INDEX idx_weather_hourly_hour ON weather_hourly (hour_of_day);