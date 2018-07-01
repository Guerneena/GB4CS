package com.hklouch.domain.model

data class ProjectList(val nextPage: String,
                       val projects: List<Project>) : Iterable<Project> by projects {
    val size = projects.size
}