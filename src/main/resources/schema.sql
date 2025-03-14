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