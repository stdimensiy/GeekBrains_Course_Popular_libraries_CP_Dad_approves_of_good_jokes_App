package ru.vdv.myapp.dadapproves.presentation.contentview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.vdv.myapp.dadapproves.App
import ru.vdv.myapp.dadapproves.databinding.FragmentContentViewBinding
import ru.vdv.myapp.dadapproves.presentation.interfaces.BackButtonListener
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainView
import ru.vdv.myapp.dadapproves.presentation.main.MainPresenter
import ru.vdv.myapp.mygitapiapp.myschedulers.MySchedulersFactory

class ContentViewFragment : MvpAppCompatFragment(), MainView, BackButtonListener {
    private var vb: FragmentContentViewBinding? = null
    private val presenter: MainPresenter by moxyPresenter {
        MainPresenter(
            MySchedulersFactory.create(),
            App.instance.router
        )
    }

    companion object {
        fun newInstance() = ContentViewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        FragmentContentViewBinding.inflate(inflater, container, false).also {
            vb = it
        }.root

    override fun init() {
        //TODO("Not yet implemented")
    }

    override fun backPressed(): Boolean = presenter.backPressed()
}