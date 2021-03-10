package io.quarkus.qe.multiplepus.containers;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class MariaDbDatabaseTestResource implements QuarkusTestResourceLifecycleManager {

    private static final MariaDBContainer<?> DATABASE = new MariaDBContainer<>()
            .withDatabaseName("mariadb")
            .withUsername("mariadb")
            .withPassword("mariadb");

    @Override
    public Map<String, String> start() {
        DATABASE.start();

        return Collections.singletonMap("quarkus.datasource.\"fruits\".jdbc.url", DATABASE.getJdbcUrl());
    }

    @Override
    public void stop() {
        Optional.ofNullable(DATABASE).ifPresent(GenericContainer::stop);
    }
}
