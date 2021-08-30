package ru.vdv.myapp.dadapproves.presentation.interfaces

import moxy.viewstate.strategy.alias.SingleState

/**
 * Интерфейс экрана отображения и модерирования контента
 */
@SingleState
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
