package com.hklouch.presentation

import com.hklouch.domain.model.Branch
import com.hklouch.domain.model.Branch.Commit
import com.hklouch.domain.model.Issue
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.Project
import com.hklouch.domain.model.Pull
import com.hklouch.domain.model.User
import com.hklouch.ui.model.UiBranchItem
import com.hklouch.ui.model.UiIssueItem
import com.hklouch.ui.model.UiPagingWrapper
import com.hklouch.ui.model.UiProjectItem
import com.hklouch.ui.model.UiProjectPreviewItem
import com.hklouch.ui.model.UiPullItem
import com.hklouch.ui.model.UiUserItem

class Data {
    val domain by lazy { Domain() }
    val ui by lazy { Ui() }

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

        val branch = Branch(name = "master", head = Commit("sha", "url"))
        val branchList = PagingWrapper(nextPage = 2,
                                       lastPage = 3,
                                       items = listOf(branch, branch.copy(name = "delivery")))

        val issue = Issue(url = "Url",
                          title = "Issue",
                          ownerId = 872147,
                          ownerName = "dtrupenn",
                          ownerAvatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
                          number = 254,
                          state = "open")
        val issueList = PagingWrapper(nextPage = 2,
                                      lastPage = 3,
                                      items = listOf(issue, issue.copy(title = "Second Issue")))

        val pull = Pull(url = "Url",
                        title = "Pull",
                        ownerId = 872147,
                        ownerName = "dtrupenn",
                        ownerAvatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
                        number = 1656,
                        state = "open")

        val pullList = PagingWrapper(nextPage = 2,
                                    lastPage = 3,
                                    items = listOf(pull, pull.copy(title = "another pull request")))

        val contributor = User(id = 32, name = "Jim Doe", avatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")

        val contributorList = PagingWrapper(nextPage = 2,
                                            lastPage = 3,
                                            items = listOf(contributor, contributor.copy(name = "Jonas")))
    }

    class Ui {
        private val project = Domain().project

        val uiProjectItem = UiProjectItem(
                project = project,
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

        private val uiProjectPreviewItem = UiProjectPreviewItem(project = project,
                                                                id = 3081286,
                                                                url = "https://api.github.com/repos/dtrupenn/Tetris",
                                                                name = "Tetris",
                                                                fullName = "dtrupenn/Tetris",
                                                                ownerId = 872147,
                                                                ownerName = "dtrupenn",
                                                                ownerAvatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
                                                                description = "A C implementation of Tetris using Pennsim through LC4")

        val uiPagingModel = UiPagingWrapper(nextPage = 2,
                                            lastPage = 3,
                                            items = listOf(uiProjectPreviewItem))

        val uiBranch = UiBranchItem(name = "master", sha = "sha", url = "url")

        val uiBranchList = UiPagingWrapper(nextPage = 2,
                                           lastPage = 3,
                                           items = listOf(uiBranch, uiBranch.copy(name = "delivery")))

        val uiIssue = UiIssueItem(url = "Url",
                                  title = "Issue",
                                  ownerId = 872147,
                                  ownerName = "dtrupenn",
                                  ownerAvatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
                                  number = 254,
                                  state = "open")

        val uiIssueList = UiPagingWrapper(nextPage = 2,
                                          lastPage = 3,
                                          items = listOf(uiIssue, uiIssue.copy(title = "Second Issue")))

        val uiPull = UiPullItem(url = "Url",
                                title = "Pull",
                                ownerId = 872147,
                                ownerName = "dtrupenn",
                                ownerAvatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
                                number = 1656,
                                state = "open")

        val uiPullList = UiPagingWrapper(nextPage = 2,
                                         lastPage = 3,
                                         items = listOf(uiPull, uiPull.copy(title = "another pull request")))

        val uiContributor = UiUserItem(id = 32, name = "Jim Doe", avatarUrl = "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png")

        val uiContributorList = UiPagingWrapper(nextPage = 2,
                                                lastPage = 3,
                                                items = listOf(uiContributor, uiContributor.copy(name = "Jonas")))
    }


}
