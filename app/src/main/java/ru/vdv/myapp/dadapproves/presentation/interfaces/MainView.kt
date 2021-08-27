package ru.vdv.myapp.dadapproves.presentation.interfaces

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Интерфейс главного экрана *
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    /**
     * Первичная инициализация представления
     */
    fun init()

}