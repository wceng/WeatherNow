package cn.wceng.weathernow.data.source.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cn.wceng.weathernow.data.source.local.dao.CityDao
import cn.wceng.weathernow.data.source.local.dao.WeatherDao
import cn.wceng.weathernow.data.source.local.entity.LocalCity
import cn.wceng.weathernow.data.source.local.entity.LocalWeather

@Database(
    entities = [LocalWeather::class, LocalCity::class],
    version = 1,
    exportSchema = true,
    autoMigrations = [
//        AutoMigration(1, 2)
    ]
)
@TypeConverters(value = [DataConverter::class])
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
}
