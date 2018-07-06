package com.hklouch.data.mapper

import com.hklouch.data.network.branch.BranchJson
import com.hklouch.data.network.branch.BranchJson.CommitJson
import com.hklouch.data.network.issue.IssueJson
import com.hklouch.data.network.project.ProjectJson
import com.hklouch.data.network.project.ProjectSearchResponse
import com.hklouch.data.network.pull.PullJson
import com.hklouch.data.network.user.UserJson
import com.hklouch.domain.model.Branch
import com.hklouch.domain.model.Branch.Commit
import com.hklouch.domain.model.Issue
import com.hklouch.domain.model.Project
import com.hklouch.domain.model.Pull
import com.hklouch.domain.model.User

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

        val projectList = listOf(project, project.copy(id = 32))

        val branch = Branch(name = "master", head = Commit("sha", "url"))

        val branchList = listOf(branch, branch.copy(name = "delivery"))

        val issue = Issue(url = "Url",
                          title = "Issue",
                          ownerId = 872147,
                          ownerName = "dtrupenn",
                          ownerAvatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
                          number = 254,
                          state = "open")

        val issueList = listOf(issue, issue.copy(title = "Second Issue"))

        val pull = Pull(url = "Url",
                        title = "Pull",
                        ownerId = 872147,
                        ownerName = "dtrupenn",
                        ownerAvatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
                        number = 1656,
                        state = "open")

        val pullList = listOf(pull, pull.copy(title = "another pull request"))

        val contributor = User(id = 32, name = "Jim Doe", avatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")

        val contributorList = listOf(contributor, contributor.copy(name = "Jonas"))

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

        val projectList = listOf(project, project.copy(id = 32))

        val projectSearchResponse = ProjectSearchResponse(totalCount = 3,
                                                          isIncompleteResult = false,
                                                          projects = projectList)

        val branch = BranchJson(name = "master", head = CommitJson("sha", "url"))

        val branchList = listOf(branch, branch.copy(name = "delivery"))

        val issue = IssueJson(url = "Url",
                              title = "Issue",
                              owner = owner,
                              number = 254,
                              state = "open")

        val issueList = listOf(issue, issue.copy(title = "Second Issue"))

        val pull = PullJson(url = "Url",
                            title = "Pull",
                            owner = owner,
                            number = 1656,
                            state = "open")

        val pullList = listOf(pull, pull.copy(title = "another pull request"))

        val contributor = UserJson(id = 32, name = "Jim Doe", avatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")

        val contributorList = listOf(contributor, contributor.copy(name = "Jonas"))


    }
}
