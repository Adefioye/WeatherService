-- src/main/resources/data.sql
INSERT INTO locations (code, city_name, region_name, country_name, country_code, enabled, trashed)
VALUES
    ('NYC', 'New York City', 'New York', 'United States', 'US', true, false),
    ('LAX', 'Los Angeles', 'California', 'United States', 'US', true, false),
    ('CHI', 'Chicago', 'Illinois', 'United States', 'US', true, false),
    ('MIA', 'Miami', 'Florida', 'United States', 'US', true, false),
    ('SEA', 'Seattle', 'Washington', 'United States', 'US', true, false)
ON CONFLICT (code) DO NOTHING; -- PostgreSQL syntax (adjust for your database)