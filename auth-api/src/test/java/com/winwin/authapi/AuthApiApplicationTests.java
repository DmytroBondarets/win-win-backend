package com.winwin.authapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:authapi-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "jwt.secret=abcdefghijklmnopqrstuvwxyz1234567890ABCDEF",
        "data.api.url=http://localhost:8081",
        "internal.token=winwin-internal-secret"
})
class AuthApiApplicationTests {

    @Test
    void contextLoads() {
    }
}
