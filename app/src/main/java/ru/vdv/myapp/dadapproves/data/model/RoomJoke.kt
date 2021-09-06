package ru.vdv.myapp.dadapproves.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Класс RoomJoke (создан для работы с API rzhunemogu.ru но в большей степени для хранения обработанных
 * результатов запроса) отвечает за хранение общей информации ответа сервера и сервисных данных
 *
 * @param content ........... - содержимое ответа (тело - анекдот, стишок, рассказ или афоризм)
 * @param id ................ - уникальный идентификатор контента (присваивается приложением)
 * @param type .............. - тип контента (шутки) удивительным образом совпадает с ид. группы
 * @param labelTags ......... - заголовок или набор тегов для контента
 * @param isViewedModerator ........ - пока не понял, нне попадался еще заполненный параметра (позже)
 * @param isViewedUser ............... - адрес запроса API github для получения расширенной информации о пользователе
 * @param isApproved ........... - адрес для перехода на страничку пользователя на github
 * @param isForbidden ...... - адрес запроса API github для получения выборки фолловеров (пользователей подписчиков) пользователя
 * @param estimation ...... - адрес запроса API github для получения выборки пользователей на которых подписан пользователь
 * @constructor создает объект, содержащий информацию о шутке (не только сам контент)
 */

@Parcelize
@Entity (tableName = "Jokes")
data class RoomJoke (
    val content: String,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val type: Int?,
    val labelTags: String,
    val isViewedModerator: Boolean,
    val isViewedUser: Boolean,
    val isApproved: Boolean,
    val isForbidden: Boolean,
    val estimation: Int
) : Parcelable