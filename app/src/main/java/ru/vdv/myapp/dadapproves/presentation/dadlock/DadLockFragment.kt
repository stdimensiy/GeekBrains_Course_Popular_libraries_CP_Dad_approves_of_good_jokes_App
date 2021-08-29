package ru.vdv.myapp.dadapproves.presentation.dadlock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.vdv.myapp.dadapproves.App
import ru.vdv.myapp.dadapproves.databinding.FragmentDadLockBinding
import ru.vdv.myapp.dadapproves.presentation.interfaces.AuthView
import ru.vdv.myapp.dadapproves.presentation.interfaces.BackButtonListener
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainView
import ru.vdv.myapp.mygitapiapp.myschedulers.MySchedulersFactory

class DadLockFragment : MvpAppCompatFragment(), MainView, AuthView, BackButtonListener {
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
        vb?.etNumberPassword?.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    Log.d("Моя проверка", "Сработал нужный мне метод")
                    //попытка авторизации пользователя
                    presenter.checkUser(vb?.etNumberPassword?.editableText.toString())
                }
            }
            false
        }
    }

    override fun backPressed(): Boolean = presenter.backPressed()

    override fun showErrorMessage(){
        vb?.tvErrorMessage?.visibility = View.VISIBLE

    }

    override fun hideErrorMessage() {
        vb?.tvErrorMessage?.visibility = View.GONE
    }

    override fun clearPasswordView() {
        vb?.etNumberPassword?.setText("")
    }
}