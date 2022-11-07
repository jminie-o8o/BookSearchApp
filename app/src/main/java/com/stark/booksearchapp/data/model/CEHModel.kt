package com.stark.booksearchapp.data.model

data class CEHModel(
    val throwable: Throwable?,
    val errorMessage: String?,
) {
    companion object {
        const val INITIAL_MESSAGE = ""
    }
}
