# KokoSky Inc Weather API service

#### Tasks done so far
- Created __version 1.0.0__ of the API documentation using __Swagger__ based on OPEN API specification.
- Created __add, delete, get, and update__ locations API with 100% unit test coverage of all methods in repository, service and controller using `jUnit and mockito`.

#### Changes to be made
- [x] PUT request to /api/v1/locations not /api/v1/locations/code
- [ ] DELETE location API should return __204__ and not __200__ status code. Also delete operation is just supposed to set
`trashed` field to `true`.
- [ ] Add __logger factory__ to GlobalErrorHandler.
- [ ] Add custom error message to @NotBlank validation annotation in location entity fields
- [ ] Add list of errors to APIError in order to capture multiple validation errors for location entity values.

## Essential docker commands used in project
- `docker-compose up -d` to start the postgresSQL container
- `docker ps` to verify postgres is running
- `docker exec -it <container_name> psql -U koko -d weatherdb`
- Inside psql shell, `\l` to check if weatherdb exists, `\dt` to list tables, `\q` to exit the psql shell.