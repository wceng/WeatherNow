package cn.wceng.weathernow.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class SavableCity(
    val city: City,
    val saved: Boolean
)
