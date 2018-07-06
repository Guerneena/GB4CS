package com.hklouch.githubrepos4cs.data.mapper

import com.hklouch.data.network.project.ProjectJson
import com.hklouch.data.network.project.ProjectSearchResponse
import com.hklouch.data.network.user.UserJson
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.Project

class Data {

    val domain by lazy { Domain() }
    val json by lazy { Json() }

    class Domain {

        val project = Project(
                id = 3081286,
                url = "https://api.github.com/repos/dtrupenn/Tetris",
                name = "Tetris",
                fullName = "dtrupenn/Tetris",
                ownerId = 872147,
                ownerName = "dtrupenn",
                ownerAvatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
                description = "A C implementation of Tetris using Pennsim through LC4",
                collaboratorsUrl = "https://api.github.com/repos/dtrupenn/Tetris/collaborators{/collaborator}",
                isFork = false,
                starsCount = 5,
                forksCount = 2,
                issuesCount = 0,
                watchersCount = 5,
                issuesUrl = "https://api.github.com/repos/dtrupenn/Tetris/issues{/number}",
                contributorsUrl = "https://api.github.com/repos/dtrupenn/Tetris/contributors",
                branchesUrl = "https://api.github.com/repos/dtrupenn/Tetris/branches{/branch}",
                pullsUrl = "https://api.github.com/repos/dtrupenn/Tetris/pulls{/number}"
        )

        val projectList = PagingWrapper(nextPage = 2,
                                        lastPage = 3,
                                        items = listOf(project))

    }

    class Json {

        private val owner = UserJson(id = 872147,
                                     name = "dtrupenn",
                                     avatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")
        val project = ProjectJson(
                id = 3081286,
                url = "https://api.github.com/repos/dtrupenn/Tetris",
                name = "Tetris",
                fullName = "dtrupenn/Tetris",
                owner = owner,
                description = "A C implementation of Tetris using Pennsim through LC4",
                collaboratorsUrl = "https://api.github.com/repos/dtrupenn/Tetris/collaborators{/collaborator}",
                isFork = false,
                starsCount = 5,
                forksCount = 2,
                issuesCount = 0,
                watchersCount = 5,
                issuesUrl = "https://api.github.com/repos/dtrupenn/Tetris/issues{/number}",
                contributorsUrl = "https://api.github.com/repos/dtrupenn/Tetris/contributors",
                branchesUrl = "https://api.github.com/repos/dtrupenn/Tetris/branches{/branch}",
                pullsUrl = "https://api.github.com/repos/dtrupenn/Tetris/pulls{/number}"
        )

        val projectSearchResponse = ProjectSearchResponse(totalCount = 3,
                                                          isIncompleteResult = false,
                                                          projects = listOf(project))
    }
}
