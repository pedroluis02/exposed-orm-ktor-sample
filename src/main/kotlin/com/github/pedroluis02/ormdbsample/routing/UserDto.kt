package com.github.pedroluis02.ormdbsample.routing

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(val id: Int? = null, val name: String, val age: Int)
