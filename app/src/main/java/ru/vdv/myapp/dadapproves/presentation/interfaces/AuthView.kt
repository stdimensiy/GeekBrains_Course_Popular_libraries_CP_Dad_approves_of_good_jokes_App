package ru.vdv.myapp.dadapproves.presentation.interfaces

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Интерфейс экрана аутентификации пользователя (родителя) *
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface AuthView : MainView {

    /**
     * Отображение сообщения об ошибке (штатного фикстироанного)
     */
    fun showErrorMessage()

    /**
     * Сокрытие сообщения об ошибке (штатного фикстироанного)
     */
    fun hideErrorMessage()

    /**
     * Очистка поля ввода пароля от ранее введенных данных
     */
    fun clearPasswordView()
}
