package com.hklouch.data.mapper

import com.hklouch.data.model.ProjectJson
import com.hklouch.data.model.ProjectSearchResponse
import com.hklouch.domain.model.Project
import com.hklouch.domain.model.ProjectList

fun ProjectJson.toProject() = Project(id = id,
                                      url = url,
                                      name = name,
                                      fullName = fullName,
                                      ownerId = owner.id,
                                      ownerName = owner.name,
                                      ownerAvatarUrl = owner.avatarUrl,
                                      description = description,
                                      collaboratorsUrl = collaboratorsUrl,
                                      isFork = isFork,
                                      starsCount = starsCount,
                                      forksCount = forksCount,
                                      issuesCount = issuesCount,
                                      watchersCount = watchersCount,
                                      issuesUrl = issuesUrl,
                                      contributorsUrl = contributorsUrl,
                                      branchesUrl = branchesUrl,
                                      pullsUrl = pullsUrl)

fun ProjectSearchResponse.toProjectList(nextPage: Int?, lastPage: Int?) = ProjectList(nextPage = nextPage,
                                                                                      lastPage = lastPage,
                                                                                      projects = projects.map { it.toProject() })

fun Collection<ProjectJson>.toProjectList(nextPage: Int?, lastPage: Int?) = ProjectList(nextPage = nextPage,
                                                                                        lastPage = lastPage,
                                                                                        projects = map { it.toProject() })
