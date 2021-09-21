package ru.vdv.myapp.dadapproves.presentation.interfaces

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Интерфейс экрана отображения и модерирования контента
 * разметка экрана как и сам фрагмент единый как для режима просмотра
 * так и для режима модерации в зависимости от выбранного режима просто
 * скрываются ненужные и отображаются нужные виджеты
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
    fun enableBtnNext()
    fun enableBtnBack()
    fun disableBtnNext()
    fun disableBtnBack()
    fun showStatusNotVerified()
    fun showStatusVerifiedAndApproved()
    fun showStatusVerifiedAndForbidden()
    fun setTag(s: String)
    fun setContent(s: String)
    fun disableBtnApprove()
    fun disableBtnForbidden()
    fun enableBtnApprove()
    fun enableBtnForbidden()
    fun enableBtnLoadNewJokeFromNetwork()
    fun disableBtnLoadNewJokeFromNetwork()
}
