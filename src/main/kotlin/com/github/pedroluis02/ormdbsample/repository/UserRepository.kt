package com.github.pedroluis02.ormdbsample.repository

import com.github.pedroluis02.ormdbsample.service.User
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepository {

    suspend fun create(user: User): User = dbQuery {
        val generatedId = Users.insert {
            it[name] = user.name
            it[age] = user.age
        }[Users.id]

        user.copy(id = generatedId)
    }

    suspend fun readAll(): List<User> = dbQuery {
        Users.selectAll()
            .map(::toModel)
    }

    suspend fun read(id: Int): User? = dbQuery {
        Users.selectAll()
            .where { Users.id eq id }
            .map(::toModel)
            .singleOrNull()
    }

    suspend fun update(user: User) {
        dbQuery {
            Users.update({ Users.id eq user.id }) {
                it[name] = user.name
                it[age] = user.age
            }
        }
    }

    suspend fun delete(id: Int) = dbQuery {
        Users.deleteWhere { Users.id.eq(id) }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

private fun toModel(it: ResultRow): User = User(it[Users.id], it[Users.name], it[Users.age])