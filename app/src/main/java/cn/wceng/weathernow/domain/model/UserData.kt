package cn.wceng.weathernow.domain.model

data class UserData(
    val savedCityIds: Set<String> = emptySet(),
    val currentCityId: String? = null
)