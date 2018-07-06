package com.hklouch.data.network.branch

import com.hklouch.data.network.branch.BranchJson.CommitJson
import com.hklouch.domain.model.Branch
import com.hklouch.domain.model.Branch.Commit

fun BranchJson.toBranch() = Branch(name = name,
                                   head = head.toCommit())

fun CommitJson.toCommit() = Commit(url = url,
                                   sha = sha)

fun Collection<BranchJson>.toBranches() = map { it.toBranch() }