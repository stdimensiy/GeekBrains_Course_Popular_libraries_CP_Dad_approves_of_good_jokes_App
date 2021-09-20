package ru.vdv.myapp.dadapproves.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.vdv.myapp.dadapproves.App
import ru.vdv.myapp.dadapproves.data.model.JokesRepository
import ru.vdv.myapp.dadapproves.data.retrofit.RNApiFactory
import ru.vdv.myapp.dadapproves.data.storage.MyStorageFactory
import ru.vdv.myapp.dadapproves.databinding.FragmentMainBinding
import ru.vdv.myapp.dadapproves.myschedulers.MySchedulersFactory
import ru.vdv.myapp.dadapproves.presentation.interfaces.BackButtonListener
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainFragmentView

class MainFragment : MvpAppCompatFragment(), MainFragmentView, BackButtonListener {
    private var vb: FragmentMainBinding? = null
    private val presenter: MainPresenter by moxyPresenter {
        MainPresenter(
            requireContext(),
            MySchedulersFactory.create(),
            JokesRepository(RNApiFactory.create(), MyStorageFactory.create(requireContext())),
            App.instance.router
        )
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        FragmentMainBinding.inflate(inflater, container, false).also {
            vb = it
        }.root

    override fun init() {
        vb?.cvCategoryAnecdotes?.setOnClickListener { presenter.goToContentView(1) }
        vb?.cvStories?.setOnClickListener { presenter.goToContentView(2) }
        vb?.cvPoems?.setOnClickListener { presenter.goToContentView(3) }
        vb?.cvAphorisms?.setOnClickListener { presenter.goToContentView(4) }
        vb?.cvDadLock?.setOnClickListener { presenter.goToDadLock() }
    }

    override fun setAnecdotesCount(t: String) {
        vb?.tvCatAnecdotesInfo?.text = t
    }

    override fun setStoriesCount(t: String) {
        vb?.tvCategoryInfoStories?.text = t
    }

    override fun setPoemsCount(t: String) {
        vb?.tvCategoryInfoPoems?.text = t
    }

    override fun setAphorismsCount(t: String) {
        vb?.tvCategoryInfoAphorisms?.text = t
    }

    override fun backPressed(): Boolean = presenter.backPressed()
}