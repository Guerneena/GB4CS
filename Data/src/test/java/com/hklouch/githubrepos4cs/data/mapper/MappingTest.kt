package com.hklouch.githubrepos4cs.data.mapper

import com.google.common.truth.Truth
import com.hklouch.data.mapper.toProject
import com.hklouch.data.mapper.toProjectList
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
        Truth.assertThat(projectData.json.projectSearchResponse.toProjectList(nextPage = 2, lastPage = 4))
                .isEqualTo(projectData.domain.projectList.copy(nextPage = 2, lastPage = 4))

    }

    @Test
    fun should_map_collection_of_project_json_to_project_list() {
        val projectJsonCollection = listOf(projectData.json.project)
        Truth.assertThat(projectJsonCollection.toProjectList(nextPage = 2, lastPage = 4))
                .isEqualTo(projectData.domain.projectList.copy(nextPage = 2, lastPage = 4))

    }
}