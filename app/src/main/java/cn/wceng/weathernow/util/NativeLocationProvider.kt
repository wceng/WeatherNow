package cn.wceng.weathernow.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class NativeLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /**
     * 获取一次当前位置（最优可用来源）
     * @param timeoutMs 超时时间（毫秒）
     */
    fun getCurrentLocation(timeoutMs: Long = 5000): Flow<LocationResult> = callbackFlow {
        // 检查权限
        if (!hasLocationPermission()) {
            trySend(LocationResult.Error("Location permission denied"))
            close()
            return@callbackFlow
        }

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                trySend(LocationResult.Success(location))
                close() // 获取成功后立即关闭流
            }

            override fun onProviderDisabled(provider: String) {
                trySend(LocationResult.Error("Provider $provider disabled"))
                close()
            }
        }

        // 优先使用GPS，其次网络定位
        val providers = listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER
        ).filter { locationManager.isProviderEnabled(it) }

        if (providers.isEmpty()) {
            trySend(LocationResult.Error("No available location providers"))
            close()
            return@callbackFlow
        }

        // 请求位置更新
        providers.forEach { provider ->
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.requestSingleUpdate(
                    provider,
                    locationListener,
                    Looper.getMainLooper()
                )
            }
        }

        // 超时处理
        val timeoutRunnable = Runnable {
            trySend(LocationResult.Error("Location request timeout"))
            close()
        }
        android.os.Handler(Looper.getMainLooper()).postDelayed(timeoutRunnable, timeoutMs)

        // 清理资源
        awaitClose {
            android.os.Handler(Looper.getMainLooper()).removeCallbacks(timeoutRunnable)
            locationManager.removeUpdates(locationListener)
        }
    }.onStart { emit(LocationResult.Loading) }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    sealed interface LocationResult {
        data object Loading : LocationResult
        data class Success(val location: Location) : LocationResult
        data class Error(val message: String) : LocationResult
    }
}