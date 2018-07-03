package com.hklouch.ui.browse

import com.hklouch.domain.model.Project

interface ProjectListener {

    fun onProjectClicked(project: Project)

}