package ru.vdv.myapp.dadapproves.presentation.dadsoffice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.vdv.myapp.dadapproves.App
import ru.vdv.myapp.dadapproves.databinding.FragmentDadsOfficeBinding
import ru.vdv.myapp.dadapproves.presentation.interfaces.BackButtonListener
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainView
import ru.vdv.myapp.mygitapiapp.myschedulers.MySchedulersFactory

class DadsOfficeFragment : MvpAppCompatFragment(), MainView, BackButtonListener {
    private var vb: FragmentDadsOfficeBinding? = null
    private val presenter: DadsOfficePresenter by moxyPresenter {
        DadsOfficePresenter(
            MySchedulersFactory.create(),
            App.instance.router
        )
    }

    companion object {
        fun newInstance() = DadsOfficeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        FragmentDadsOfficeBinding.inflate(inflater, container, false).also {
            vb = it
        }.root

    override fun init() {
        vb?.cvCategoryJokes?.setOnClickListener { presenter.goToContentView(1) }
        vb?.cvStories?.setOnClickListener { presenter.goToContentView(2) }
        vb?.cvPoems?.setOnClickListener { presenter.goToContentView(3) }
        vb?.cvAfor?.setOnClickListener { presenter.goToContentView(4) }
    }

    override fun backPressed(): Boolean = presenter.backPressed()
}