package com.hklouch.data.mapper

import com.google.common.truth.Truth
import com.hklouch.data.network.branch.toBranch
import com.hklouch.data.network.branch.toBranches
import com.hklouch.data.network.issue.toIssue
import com.hklouch.data.network.issue.toIssues
import com.hklouch.data.network.project.toProject
import com.hklouch.data.network.project.toProjectList
import com.hklouch.data.network.pull.toPull
import com.hklouch.data.network.pull.toPulls
import com.hklouch.data.network.user.toUser
import com.hklouch.data.network.user.toUsers
import org.junit.Before
import org.junit.Test

class MappingTest {

    private lateinit var projectData: Data

    @Before
    fun before() {
        projectData = Data()
    }

    @Test
    fun should_map_project_json_to_project() {
        Truth.assertThat(projectData.json.project.toProject()).isEqualTo(projectData.domain.project)

    }

    @Test
    fun should_map_project_search_response_to_project_list() {
        Truth.assertThat(projectData.json.projectSearchResponse.toProjectList())
                .isEqualTo(projectData.domain.projectList)

    }

    @Test
    fun should_map_collection_of_project_json_to_project_list() {
        val projectJsonCollection = projectData.json.projectList
        Truth.assertThat(projectJsonCollection.toProjectList())
                .isEqualTo(projectData.domain.projectList)

    }

    @Test
    fun should_map_branch_json_to_branch() {
        Truth.assertThat(projectData.json.branch.toBranch()).isEqualTo(projectData.domain.branch)

    }

    @Test
    fun should_map_collection_of_branch_json_to_branch_list() {
        val jsonCollection = projectData.json.branchList
        Truth.assertThat(jsonCollection.toBranches())
                .isEqualTo(projectData.domain.branchList)

    }


    @Test
    fun should_map_pull_json_to_pull() {
        Truth.assertThat(projectData.json.pull.toPull()).isEqualTo(projectData.domain.pull)

    }

    @Test
    fun should_map_collection_of_pull_json_to_pull_list() {
        val jsonCollection = projectData.json.pullList
        Truth.assertThat(jsonCollection.toPulls())
                .isEqualTo(projectData.domain.pullList)

    }

    @Test
    fun should_map_contributor_json_to_contributor() {
        Truth.assertThat(projectData.json.contributor.toUser()).isEqualTo(projectData.domain.contributor)

    }

    @Test
    fun should_map_collection_of_contributor_json_to_contributor_list() {
        val jsonCollection = projectData.json.contributorList
        Truth.assertThat(jsonCollection.toUsers())
                .isEqualTo(projectData.domain.contributorList)

    }

    @Test
    fun should_map_issue_json_to_issue() {
        Truth.assertThat(projectData.json.issue.toIssue()).isEqualTo(projectData.domain.issue)

    }

    @Test
    fun should_map_collection_of_issue_json_to_issue_list() {
        val jsonCollection = projectData.json.issueList
        Truth.assertThat(jsonCollection.toIssues())
                .isEqualTo(projectData.domain.issueList)

    }
}