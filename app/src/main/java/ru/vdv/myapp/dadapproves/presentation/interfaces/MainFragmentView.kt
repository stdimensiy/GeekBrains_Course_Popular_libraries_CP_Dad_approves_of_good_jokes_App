package ru.vdv.myapp.dadapproves.presentation.interfaces

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainFragmentView : MainView {
    fun setAnecdotesCount(t: String)
    fun setStoriesCount(t: String)
    fun setPoemsCount(t: String)
    fun setAphorismsCount(t: String)
}