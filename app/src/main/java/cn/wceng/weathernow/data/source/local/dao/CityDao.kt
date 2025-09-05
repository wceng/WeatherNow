package cn.wceng.weathernow.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.wceng.weathernow.data.source.local.entity.LocalCity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: LocalCity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<LocalCity>)

    @Query("SELECT * FROM cities WHERE id = :cityId")
    fun getCityById(cityId: String): Flow<LocalCity>

    @Query("SELECT * FROM cities WHERE id IN (:cityIds)")
    fun getCitiesByIds(cityIds: Set<String>): Flow<List<LocalCity>>

    @Query("SELECT * FROM cities WHERE name LIKE '%' || :query || '%' OR district LIKE '%' || :query || '%'")
    fun searchCities(query: String): Flow<List<LocalCity>>

    @Query("SELECT EXISTS(SELECT 1 FROM cities WHERE id = :cityId LIMIT 1)")
    fun isCityExists(cityId: String): Flow<Boolean>

    @Query("delete from cities where id = :cityId")
    suspend fun deleteCity(cityId: String)
}