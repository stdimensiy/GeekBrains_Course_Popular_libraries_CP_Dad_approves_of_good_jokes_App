package ru.vdv.myapp.dadapproves.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Класс RoomJoke (создан для работы с внутренней базой данных, для хранения обработанных данных
 *
 * @param content ........... - содержимое ответа (тело - анекдот, стишок, рассказ или афоризм)
 * @param id ................ - уникальный идентификатор контента (присваивается приложением)
 * @param type .............. - тип контента (шутки) удивительным образом совпадает с ид. группы
 * @param labelTags ......... - заголовок или набор тегов для контента (шутки)
 * @param isViewedModerator . - признак того, что шутка просмотрена модератором
 * @param isViewedUser ...... - признак того, что шутка просмотрена пользователем
 * @param isApproved ........ - признак того, что шутка одобрена
 * @param isForbidden ....... - признак того, что шутка отклонена (заблокирована)
 * @param estimation ........ - популярность
 * @constructor создает объект, содержащий информацию о шутке (не только сам контент)
 */

@Parcelize
@Entity(tableName = "Jokes")
data class RoomJoke(
    val content: String,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val type: Int?,
    var labelTags: String,
    var isViewedModerator: Boolean,
    val isViewedUser: Boolean,
    var isApproved: Boolean,
    var isForbidden: Boolean,
    val estimation: Int
) : Parcelable