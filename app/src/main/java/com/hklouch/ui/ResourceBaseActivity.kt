package com.hklouch.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.browse.BrowseFragment
import dagger.android.AndroidInjection

abstract class ResourceBaseActivity: AppCompatActivity(){

    companion object {

        internal const val OWNER_NAME_EXTRA = "owner"
        internal const val PROJECT_NAME_EXTRA = "project"

        fun <T : ResourceBaseActivity> createIntent(source: Activity,
                                                    destination: Class<T>,
                                                    ownerName: String,
                                                    projectName: String): Intent {
            return Intent(source, destination)
                    .apply {
                        putExtra(ResourceBaseActivity.OWNER_NAME_EXTRA, ownerName)
                        putExtra(ResourceBaseActivity.PROJECT_NAME_EXTRA, projectName)
                    }
        }

    }

    protected lateinit var ownerName: String
    protected lateinit var projectName: String

    /* ***************** */
    /*     Life cycle    */
    /* ***************** */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ownerName = intent.getStringExtra(OWNER_NAME_EXTRA)
        projectName = intent.getStringExtra(PROJECT_NAME_EXTRA)

        setContentView(R.layout.browse_projects_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp() = finish().let { true }


}
