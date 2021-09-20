package ru.vdv.myapp.dadapproves.presentation.interfaces

import com.github.terrakok.cicerone.Screen

/**
 * Общий интерфейс экранов приложения
 *
 * организационный модуль, в котором принципиально объявляются экраны приложения
 */
interface IScreens {
    /**
     * Главный загрузочный экран (режимы работы приложеня, доступные рубрики и шутки)
     */
    fun main(): Screen

    /**
     * Экран аутентификации пользователя
     */
    fun dadLock(): Screen

    /**
     * Экран выбора режима просмотра и модерации материала (закрытый раздел приложения)
     */
    fun dadsOffice(): Screen

    /**
     * Экран просмотра и модерации контента
     */
    fun contentView(moderationMode: Boolean, category: Int): Screen
}