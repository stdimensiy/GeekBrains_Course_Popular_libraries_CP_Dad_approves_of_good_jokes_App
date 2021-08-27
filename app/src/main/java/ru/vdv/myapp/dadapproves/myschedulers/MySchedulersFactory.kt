package ru.vdv.myapp.mygitapiapp.myschedulers

import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers

object MySchedulersFactory {
    fun create(): IMySchedulers = MySchedulers()
}