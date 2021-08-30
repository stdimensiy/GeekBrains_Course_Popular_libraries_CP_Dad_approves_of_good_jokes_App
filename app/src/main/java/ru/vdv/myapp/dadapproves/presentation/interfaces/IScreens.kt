package ru.vdv.myapp.dadapproves.presentation.interfaces

import com.github.terrakok.cicerone.Screen

/**
 * Общий интерфейс экранов прриложения
 *
 * организационный модуль, в котором принципиально объявляются экраны приложения
 */
interface IScreens {
    /**
     * Главный загрузочный экран (режимы работы приложеня, доступные рубрики)
     */
    fun main(): Screen

    /**
     * Экран авторизации пользователя
     */
    fun dadLock(): Screen

    /**
     * Экран выбора режима просмотра и модерации материала
     */
    fun dadsOffice(): Screen

    /**
     * Экран просмотра контента
     */
    fun contentView(moderationMode: Boolean, category: Int): Screen
}