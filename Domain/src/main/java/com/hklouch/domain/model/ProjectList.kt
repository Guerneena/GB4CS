package com.hklouch.domain.model

data class ProjectList(val nextPage: Int?,
                       val lastPage: Int?,
                       val projects: List<Project>) : Iterable<Project> by projects {
    val size = projects.size
}