package ru.vdv.myapp.dadapproves.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Класс Joke (создан для работы с API rzhunemogu.ru т.к. ответ сервера содержит очеь мало данных
 * расширенный набор свойств, хранящий результаты обработкки сформирован в отдельном классе
 *
 * @param content ........... - содержимое ответа (тело - анекдот, стишок, рассказ или афоризм)
 * @constructor создает объект, содержащий информацию о шутке - сам контент
 */

@Parcelize
data class Joke(
    @SerializedName("content")
    val content: String,
) : Parcelable
