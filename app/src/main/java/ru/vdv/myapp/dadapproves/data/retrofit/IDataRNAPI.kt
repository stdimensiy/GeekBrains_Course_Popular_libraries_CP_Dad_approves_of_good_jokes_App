package ru.vdv.myapp.dadapproves.data.retrofit

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.vdv.myapp.dadapproves.data.model.Joke

interface IDataRNAPI {

    /**
     * Позволяет получить случайную шутку заданной категории CType
     * c сервера rzhuNemagu.ru
     */
    @GET("/RandJSON.aspx")
    fun getContent(
        @Query("CType") groupId: Int
    ): Single<Joke>
}
