package com.example.fixedcostmanagement

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class FixedCostManagementApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val builder = RealmConfiguration.Builder()
        // builder.schemaVersion(1L).migration(Migration())
        val config = builder.deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }
}