package ru.vdv.myapp.dadapproves.presentation.interfaces

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Интерфейс экрана отображения и модерирования контента
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ContentView : MainView {

    fun showProgressBar()
    fun hideProgressBar()
    fun showErrorBar()
    fun hideErrorBar()
    fun moderatorModeInit()
    fun showModeratorBtnGroup()
    fun hideModeratorBtnGroup()
    fun clearStatus()
    fun showStatusNotVerified()
    fun showStatusVerifiedAndApproved()
    fun showStatusVerifiedAndForbidden()
    fun setTag(s: String)
    fun setContent(s: String)
}
