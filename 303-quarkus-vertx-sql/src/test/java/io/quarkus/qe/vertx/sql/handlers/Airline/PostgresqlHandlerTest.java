package io.quarkus.qe.vertx.sql.handlers.Airline;

import io.quarkus.qe.vertx.sql.test.resources.PostgresqlTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(PostgresqlTestProfile.class)
public class PostgresqlHandlerTest extends AirlineHandlerTest{
}