package com.hklouch.githubrepos4cs.data.mapper

import com.hklouch.data.model.OwnerJson
import com.hklouch.data.model.ProjectJson
import com.hklouch.data.model.ProjectSearchResponse
import com.hklouch.domain.model.Project
import com.hklouch.domain.model.ProjectList

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

        val projectList = ProjectList(nextPage = 2,
                                      lastPage = 3,
                                      projects = listOf(project))

    }

    class Json {

        private val owner = OwnerJson(id = 872147,
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
