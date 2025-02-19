package com.github.pedroluis02.ormdbsample.service

import com.github.pedroluis02.ormdbsample.repository.UserRepository

class UserService(private val repository: UserRepository = UserRepository()) {

    suspend fun readAll() = repository.readAll()

    suspend fun create(user: User) = repository.create(user)

    suspend fun read(id: Int) = repository.read(id)

    suspend fun update(user: User) = repository.update(user)

    suspend fun delete(id: Int) = repository.delete(id)
}
