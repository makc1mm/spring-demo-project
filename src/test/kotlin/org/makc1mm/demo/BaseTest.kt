package org.makc1mm.demo

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.Connection
import java.sql.DriverManager

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [BaseTest.Companion.CustomInitializer::class])
abstract class BaseTest {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    protected val objectMapper = ObjectMapper()
    protected val headers = HttpHeaders().apply {
        set("Content-Type", "application/json")
    }

    lateinit var database: Connection

    companion object {
        const val baseUrl = "/api/person"
        private const val postgresBase = "test"
        private const val postgresUser = "test"
        private const val postgresPassword = "test"

        @Container
        private val container = PostgreSQLContainer<Nothing>("postgres:12.1").apply {
            withDatabaseName(postgresBase)
            withUsername(postgresUser)
            withPassword(postgresPassword)
            start()
        }
        internal class CustomInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(applicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of(
                    "spring.datasource.url=${container.jdbcUrl}",
                    "spring.datasource.username=$postgresUser",
                    "spring.datasource.password=$postgresPassword"
                ).applyTo(applicationContext)
            }
        }
    }

    @BeforeAll
    fun start() {
        database = DriverManager.getConnection("${container.jdbcUrl}&user=$postgresUser&password=$postgresPassword")
        logger.info("### Database is connected ###")
        logger.info("### URL: ${container.jdbcUrl} ###")
    }

    @AfterAll
    fun stop() {
        container.stop()
    }
}