package com.hklouch.data.model

import com.hklouch.domain.model.Project
import com.hklouch.domain.model.ProjectList

fun ProjectListJson.toProjectList() = ProjectList(nextPage = nextPage,
                                                  projects = projects.map { it.toProject() })

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
                                      starCount = starCount,
                                      watchersCount = watchersCount,
                                      issuesUrl = issuesUrl,
                                      contributorsUrl = contributorsUrl,
                                      branchesUrl = branchesUrl)
