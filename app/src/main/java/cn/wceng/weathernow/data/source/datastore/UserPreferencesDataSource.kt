package cn.wceng.weathernow.data.source.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import cn.wceng.weathernow.data.datastore.UserPreferences
import cn.wceng.weathernow.domain.model.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
        fileName = "user_prefs.pb",
        serializer = UserPreferencesSerializer,
    )

    val userData: Flow<UserData> =
        context.userPreferencesStore.data
            .map { preferences ->
                UserData(
                    savedCityIds = preferences.savedCitiesMap.keys,
                    currentCityId = preferences.currentCityId.ifEmpty { null }
                )
            }

    suspend fun setSavedCityIds(cityIds: Set<String>) {
        context.userPreferencesStore.updateData { currentPrefs ->
            currentPrefs
                .toBuilder()
                .clearSavedCities()
                .putAllSavedCities(cityIds.associateWith { true })
                .build()
        }
    }

    suspend fun setCitySaved(
        cityId: String,
        saved: Boolean,
    ) {
        context.userPreferencesStore.updateData { currentPrefs ->
            currentPrefs
                .toBuilder()
                .apply {
                    if (saved) {
                        putSavedCities(cityId, true)
                    } else {
                        removeSavedCities(cityId)
                    }
                }.build()
        }
    }

    suspend fun setCurrentCityId(cityId: String) {
        context.userPreferencesStore.updateData { currentPrefs ->
            currentPrefs.toBuilder().apply {
                currentCityId = cityId
            }.build()
        }
    }
}
