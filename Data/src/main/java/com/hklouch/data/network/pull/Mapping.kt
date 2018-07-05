package com.hklouch.data.network.pull

import com.hklouch.domain.interactor.pull.PullsUseCase
import com.hklouch.domain.interactor.pull.PullsUseCase.Params
import com.hklouch.domain.model.Pull

fun PullJson.toPull() = Pull(url = url,
                             ownerId = owner.id,
                             ownerName = owner.name,
                             ownerAvatarUrl = owner.avatarUrl,
                             title = title,
                             number = number,
                             state = state)

fun Collection<PullJson>.toPulls() = map { it.toPull() }

fun Params.State.toParam() = when (this) {

    PullsUseCase.Params.State.OPEN -> "open"
    PullsUseCase.Params.State.CLOSED -> "closed"
    PullsUseCase.Params.State.ALL -> "all"
}