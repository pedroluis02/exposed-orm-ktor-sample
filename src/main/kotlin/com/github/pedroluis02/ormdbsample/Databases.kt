package com.github.pedroluis02.ormdbsample

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        driverClassName = "org.h2.Driver"
        username = "root"
        password = ""
        maximumPoolSize = 6
        isReadOnly = false
    }
    val dataSource = HikariDataSource(config)

    val database = Database.connect(
        datasource = dataSource,
        databaseConfig = DatabaseConfig()
    )

    transaction(database) {
        SchemaUtils.create(Users)
    }
}
