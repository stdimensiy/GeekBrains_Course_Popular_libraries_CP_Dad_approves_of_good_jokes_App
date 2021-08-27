package ru.vdv.myapp.dadapproves.presentation.dadlock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.vdv.myapp.dadapproves.App
import ru.vdv.myapp.dadapproves.databinding.FragmentDadLockBinding
import ru.vdv.myapp.dadapproves.presentation.contentview.ContentViewFragment
import ru.vdv.myapp.dadapproves.presentation.interfaces.BackButtonListener
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainView
import ru.vdv.myapp.mygitapiapp.myschedulers.MySchedulersFactory

class DadLockFragment : MvpAppCompatFragment(), MainView, BackButtonListener {
    private var vb: FragmentDadLockBinding? = null
    private val presenter: DadLockPresenter by moxyPresenter {
        DadLockPresenter(
            MySchedulersFactory.create(),
            App.instance.router
        )
    }

    companion object {
        fun newInstance() = DadLockFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        FragmentDadLockBinding.inflate(inflater, container, false).also {
            vb = it
        }.root

    override fun init() {
        //TODO("Not yet implemented")
    }

    override fun backPressed(): Boolean = presenter.backPressed()
}