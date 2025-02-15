package com.github.pedroluis02.ormdbsample

import kotlinx.serialization.Serializable

@Serializable
data class ExposedUser(val name: String, val age: Int)
