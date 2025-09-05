package cn.wceng.weathernow.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.wceng.weathernow.data.source.local.entity.LocalWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: LocalWeather)

    @Query("SELECT * FROM weather WHERE location = :location")
    fun getWeatherStream(location: String): Flow<LocalWeather?>

    @Query("select * from weather where location in (:locations)")
    fun getWeathersStream(locations: Set<String>): Flow<List<LocalWeather>>

    @Query("DELETE FROM weather WHERE last_fetched < :timestamp")
    suspend fun deleteOldData(timestamp: Long)
}
