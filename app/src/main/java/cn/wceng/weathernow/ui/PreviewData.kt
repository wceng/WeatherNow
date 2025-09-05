package cn.wceng.weathernow.ui

import cn.wceng.weathernow.R
import cn.wceng.weathernow.data.model.DayForecastInfo
import cn.wceng.weathernow.data.model.HourForecastInfo
import cn.wceng.weathernow.data.model.IndicesInfo
import cn.wceng.weathernow.data.model.WarningInfo
import cn.wceng.weathernow.data.model.WeatherInfo
import cn.wceng.weathernow.domain.model.City
import cn.wceng.weathernow.domain.model.CityWithWeather
import cn.wceng.weathernow.domain.model.NowWeather
import cn.wceng.weathernow.domain.model.SavableCity
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

val previewCities = listOf(
    City(
        id = "1",
        name = "北京",
        latitude = "39.9042",
        longitude = "116.4074",
        district = "北京市",
        province = "北京市",
        country = "中国",
        weatherLink = "https://weather.com/zh-CN/weather/today/l/39.90,116.41"
    ),
    City(
        id = "2",
        name = "上海",
        latitude = "31.2304",
        longitude = "121.4737",
        district = "上海市",
        province = "上海市",
        country = "中国",
        weatherLink = "https://weather.com/zh-CN/weather/today/l/31.23,121.47"
    ),
    City(
        id = "3",
        name = "广州",
        latitude = "23.1291",
        longitude = "113.2644",
        district = "天河区",
        province = "广东省",
        country = "中国",
        weatherLink = "https://weather.com/zh-CN/weather/today/l/23.13,113.26"
    )
)

val previewCity = previewCities.first()

val previewSavableCity = SavableCity(
    city = previewCity,
    saved = true
)

val previewWeatherInfos = listOf(
    WeatherInfo(
        location = "北京市",
        updateTime = Instant.fromEpochMilliseconds(System.currentTimeMillis()),
        obsTime = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1800000), // 30分钟前
        temp = 25,
        feelsLike = 27,
        icon = "100", // 晴
        text = "晴朗",
        windDir = "东南风",
        windScale = "3级",
        humidity = "45%",
        hourForecastInfos = listOf(),
        dayForecastInfos = listOf(),
        warningInfos = emptyList(),
        indicesInfos = emptyList()
    ),
    WeatherInfo(
        location = "上海市",
        updateTime = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 3600000), // 1小时前
        obsTime = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 5400000), // 1.5小时前
        temp = 22,
        feelsLike = 24,
        icon = "101", // 多云
        text = "多云",
        windDir = "东风",
        windScale = "2级",
        humidity = "60%",
        hourForecastInfos = listOf(),
        dayForecastInfos = listOf(),
        warningInfos = emptyList(),
        indicesInfos = emptyList()
    ),
    WeatherInfo(
        location = "广州市",
        updateTime = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 7200000), // 2小时前
        obsTime = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 9000000), // 2.5小时前
        temp = 28,
        feelsLike = 32,
        icon = "104", // 阴天
        text = "阴天",
        windDir = "南风",
        windScale = "1级",
        humidity = "75%",
        hourForecastInfos = listOf(),
        dayForecastInfos = listOf(),
        warningInfos = emptyList(),
        indicesInfos = emptyList()
    )
)

val previewCityWithWeathers = listOf(
    CityWithWeather(
        city = previewCities[0],
        weatherInfo = previewWeatherInfos[0]
    ),
    CityWithWeather(
        city = previewCities[1],
        weatherInfo = previewWeatherInfos[1]
    ),
    CityWithWeather(
        city = previewCities[2],
        weatherInfo = previewWeatherInfos[2]
    ),
)


val previewHourForecastInfos = listOf(
    HourForecastInfo(
        fxTime = Instant.fromEpochMilliseconds(1724569200000), // 2024-08-25 15:00:00 UTC
        temp = 25, // 25°C
        icon = "100", // 晴
        text = "晴朗",
        wind360 = "135",
        windDir = "东南风"
    ),
    HourForecastInfo(
        fxTime = Instant.fromEpochMilliseconds(1724572800000), // 2024-08-25 16:00:00 UTC
        temp = 23, // 23°C
        icon = "101", // 多云
        text = "多云",
        wind360 = "90",
        windDir = "东风"
    ),
    HourForecastInfo(
        fxTime = Instant.fromEpochMilliseconds(1724576400000), // 2024-08-25 17:00:00 UTC
        temp = 20, // 20°C
        icon = "300", // 雨
        text = "小雨",
        wind360 = "180",
        windDir = "南风"
    )
)

val previewDayForecastInfos = listOf(
    DayForecastInfo(
        fxDate = Instant.fromEpochMilliseconds(1724544000000), // 2024-08-25
        tempMax = 28, // 最高28°C
        tempMin = 18, // 最低18°C
        iconDay = "100", // 白天晴
        textDay = "晴",
        iconNight = "150", // 夜间晴
        textNight = "晴"
    ),
    DayForecastInfo(
        fxDate = Instant.fromEpochMilliseconds(1724630400000), // 2024-08-26
        tempMax = 26, // 最高26°C
        tempMin = 16, // 最低16°C
        iconDay = "101", // 白天多云
        textDay = "多云转小雨",
        iconNight = "151", // 夜间多云
        textNight = "多云"
    ),
    DayForecastInfo(
        fxDate = Instant.fromEpochMilliseconds(1724716800000), // 2024-08-27
        tempMax = 22, // 最高22°C
        tempMin = 14, // 最低14°C
        iconDay = "300", // 白天下雨
        textDay = "小雨",
        iconNight = "300", // 夜间下雨
        textNight = "小雨"
    )
)

val previewWarningInfos = listOf(
    WarningInfo(
        sender = "上海中心气象台",
        pubTime = Instant.parse("2023-04-03T10:30:00+08:00"),
        title = "上海中心气象台发布大风蓝色预警[Ⅳ级/一般]",
        startTime = Instant.parse("2023-04-03T10:30:00+08:00"),
        endTime = Instant.parse("2023-04-04T10:30:00+08:00"),
        status = "active",
        severity = "Minor",
        severityColor = "Blue",
        type = "1006",
        typeName = "大风",
        urgency = "Immediate",
        certainty = "Observed",
        text = "上海中心气象台2023年04月03日10时30分发布大风蓝色预警[Ⅳ级/一般]：受江淮气旋影响，预计明天傍晚以前本市大部地区将出现6级阵风7-8级的东南大风，沿江沿海地区7级阵风8-9级，请注意防范大风对高空作业、交通出行、设施农业等的不利影响。"
    ),
    WarningInfo(
        sender = "北京市气象台",
        pubTime = Instant.parse("2023-07-15T14:20:00+08:00"),
        title = "北京市气象台发布暴雨黄色预警[Ⅲ级/较重]",
        startTime = Instant.parse("2023-07-15T14:20:00+08:00"),
        endTime = Instant.parse("2023-07-16T02:20:00+08:00"),
        status = "active",
        severity = "Moderate",
        severityColor = "Yellow",
        type = "1003",
        typeName = "暴雨",
        urgency = "Expected",
        certainty = "Likely",
        text = "北京市气象台2023年07月15日14时20分发布暴雨黄色预警[Ⅲ级/较重]：预计15日傍晚至16日凌晨，本市大部分地区将出现50毫米以上的降水，局地可达100毫米以上，并伴有短时强降水、雷暴大风等强对流天气，请注意防范。"
    ),
    WarningInfo(
        sender = "广东省气象台",
        pubTime = Instant.parse("2023-08-10T09:15:00+08:00"),
        title = "广东省气象台发布高温红色预警[Ⅰ级/特别严重]",
        startTime = Instant.parse("2023-08-10T09:15:00+08:00"),
        endTime = Instant.parse("2023-08-12T18:00:00+08:00"),
        status = "active",
        severity = "Extreme",
        severityColor = "Red",
        type = "1009",
        typeName = "高温",
        urgency = "Future",
        certainty = "Possible",
        text = "广东省气象台2023年08月10日09时15分发布高温红色预警[Ⅰ级/特别严重]：受副热带高压影响，预计未来三天我省大部分地区最高气温将达40℃以上，请注意防暑降温，避免高温时段户外活动。"
    )
)

val previewWarningInfo = previewWarningInfos.first()

val previewIndicesInfos = listOf(
    IndicesInfo(
        date = LocalDate.parse("2023-12-16").atStartOfDayIn(TimeZone.of("+08:00")),
        type = "1",
        typeName = "运动指数",
        level = "3",
        category = "较不宜",
        description = "天气较好，但考虑天气寒冷，风力较强，推荐您进行室内运动，若户外运动请注意保暖并做好准备活动。"
    ),
    IndicesInfo(
        date = LocalDate.parse("2023-12-16").atStartOfDayIn(TimeZone.of("+08:00")),
        type = "2",
        typeName = "洗车指数",
        level = "3",
        category = "较不宜",
        description = "较不宜洗车，未来一天无雨，风力较大，如果执意擦洗汽车，要做好蒙上污垢的心理准备。"
    ),
    IndicesInfo(
        date = LocalDate.parse("2023-12-16").atStartOfDayIn(TimeZone.of("+08:00")),
        type = "8",
        typeName = "舒适度指数",
        level = "2",
        category = "较舒适",
        description = "白天天气晴好，早晚会感觉偏凉，午后舒适、宜人。"
    )
)

val previewWeatherInfo = WeatherInfo(
    location = "北京市",
    updateTime = Instant.fromEpochMilliseconds(System.currentTimeMillis()),
    obsTime = Instant.fromEpochMilliseconds(System.currentTimeMillis() - 1800000), // 30分钟前
    temp = 25,
    feelsLike = 27,
    icon = "100", // 晴
    text = "晴朗",
    windDir = "东南风",
    windScale = "3级",
    humidity = "45%",
    hourForecastInfos = previewHourForecastInfos,
    dayForecastInfos = previewDayForecastInfos,
    warningInfos = previewWarningInfos,
    indicesInfos = previewIndicesInfos
)

val previewCityWithWeather = CityWithWeather(
    city = previewCity,
    weatherInfo = previewWeatherInfo
)

val previewNowWeather = NowWeather(
    nowTemp = "32°",
    nowTempDesc = "晴朗",
    location = "唯亭，吴中区，苏州，江苏省，中国",
    feelsLike = "体感 35°",
    iconRes = R.drawable.ic_weather_100 // 请替换为您的晴天图标资源
)