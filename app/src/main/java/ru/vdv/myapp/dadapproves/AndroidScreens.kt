package ru.vdv.myapp.mygitapiapp

import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.vdv.myapp.dadapproves.presentation.contentview.ContentViewFragment
import ru.vdv.myapp.dadapproves.presentation.dadlock.DadLockFragment
import ru.vdv.myapp.dadapproves.presentation.dadsoffice.DadsOffice
import ru.vdv.myapp.dadapproves.presentation.interfaces.IScreens
import ru.vdv.myapp.dadapproves.presentation.main.MainFragment

/**
 * Класс объявления экранов"
 *
 * реализует интерфейс IScreens *
 */
class AndroidScreens : IScreens {

    /**
     *Объявление главного загрузочного экрана
     *@return возвращает главный экран (Screen)
     */
    override fun main(): Screen = FragmentScreen { MainFragment.newInstance() }

    /**
     *Объявление экрана авторизации пользователя
     *@return возвращает экран авторизации (Screen)
     */
    override fun dadLock(): Screen = FragmentScreen { DadLockFragment.newInstance() }

    /**
     *Объявление экрана выбора режима просмотра и модерации материала
     *@return возвращает управляющий экран отцовского кабинет(Screen)
     */
    override fun dadsOffice(): Screen = FragmentScreen { DadsOffice.newInstance() }

    /**
     *Объявление экрана просмотра контента
     *@return возвращает экран просмотра контента (немного разный в зависимости от режима(Screen)
     */
    override fun contentView(modeView: Int, category: Int): Screen =
        FragmentScreen { ContentViewFragment.newInstance() }
}