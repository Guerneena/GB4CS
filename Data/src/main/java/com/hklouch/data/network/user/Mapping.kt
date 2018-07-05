package com.hklouch.data.network.user

import com.hklouch.domain.model.User


fun UserJson.toUser() = User(id = id,
                             name = name,
                             avatarUrl = avatarUrl)

fun Collection<UserJson>.toUsers() = map { it.toUser() }

