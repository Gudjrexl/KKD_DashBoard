package com.jrexl.novanectorclient.objectapi

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object Datamanger {

    private val Context.dataStore by preferencesDataStore(name = "userData")

    private val clientid = stringPreferencesKey("clientid")

    suspend fun saveClient(context: Context, clid: String) {
        context.dataStore.edit { prefs ->
            prefs[clientid] = clid

        }
    }
    suspend fun getClienrid(context: Context): String? {
        return context.dataStore.data.map { it[clientid] ?: "" }.first()
    }



}