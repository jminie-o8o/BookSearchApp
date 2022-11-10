package com.stark.booksearchapp.util

import android.content.Context
import android.widget.Toast
import com.stark.booksearchapp.data.model.CEHModel
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

object CoroutineException {
    fun checkThrowableAtViewModel(throwable: Throwable): CEHModel {
        return when (throwable) {
            is SocketException -> CEHModel(throwable, "소켓 연결이 끊겼습니다.")
            is ConnectException -> CEHModel(throwable, "네트워크 연결이 불안정합니다.")
            is HttpException -> CEHModel(throwable, "Http 관련 오류입니다")
            is UnknownHostException -> CEHModel(
                throwable, "도메인 주소를 찾지 못했습니다.\n" +
                        "네트워크 상태를 확인하세요"
            )
            is NullPointerException -> CEHModel(throwable, "NullPointer 오류입니다.")
            else -> CEHModel(throwable, "알 수 없는 오류입니다.")
        }
    }

    fun checkThrowableAtView(throwable: Throwable?, context: Context) {
        when (throwable) {
            is HttpException -> Toast.makeText(
                context,
                "Http에러가 발생했습니다.",
                Toast.LENGTH_SHORT
            ).show()
            is ConnectException -> Toast.makeText(
                context,
                "네트워크 연결이 불안정합니다.",
                Toast.LENGTH_SHORT
            ).show()
            is SocketException -> Toast.makeText(
                context,
                "소켓 연결이 끊겼습니다.",
                Toast.LENGTH_SHORT
            ).show()
            is NullPointerException -> Toast.makeText(
                context,
                "NullPointer 오류입니다.",
                Toast.LENGTH_SHORT
            ).show()
            is UnknownHostException ->  Toast.makeText(
                context,
                "도메인 주소를 찾지 못했습니다.\n네트워크 상태를 확인하세요",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
