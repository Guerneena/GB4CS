package com.hklouch.data.network.project

import com.hklouch.domain.model.Project

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

fun Collection<ProjectJson>.toProjectList() = map { it.toProject() }

fun ProjectSearchResponse.toProjectList() = projects.map { it.toProject() }
