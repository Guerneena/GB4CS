package com.hklouch.data.model

data class ProjectListJson(val nextPage: String,
                           val projects: List<ProjectJson>) : Iterable<ProjectJson> by projects {
    val size = projects.size
}