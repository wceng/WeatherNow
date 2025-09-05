package cn.wceng.weathernow.data.repository

import cn.wceng.weathernow.data.source.datastore.UserPreferencesDataSource
import cn.wceng.weathernow.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setSavedCityIds(cityIds: Set<String>)

    suspend fun setCitySaved(
        cityId: String,
        saved: Boolean,
    )

    suspend fun setCurrentCityId(cityId: String?)
}

@Singleton
class OfflineFirstUserDataRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        userPreferencesDataSource.userData
            .distinctUntilChanged()

    override suspend fun setSavedCityIds(cityIds: Set<String>) {
        userPreferencesDataSource.setSavedCityIds(cityIds)
    }

    override suspend fun setCitySaved(cityId: String, saved: Boolean) {
        userPreferencesDataSource.setCitySaved(cityId, saved)
    }

    override suspend fun setCurrentCityId(cityId: String?) {
        userPreferencesDataSource.setCurrentCityId(cityId ?: "")
    }
}
