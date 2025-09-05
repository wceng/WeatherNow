package cn.wceng.weathernow.util

sealed class Result<out T> {
    data class Success<out T>(
        val data: T,
    ) : Result<T>()

    data class Error(
        val exception: Throwable,
    ) : Result<Nothing>()

    object Loading : Result<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)

        fun failure(exception: Throwable) = Error(exception)

        fun loading() = Loading
    }
}
