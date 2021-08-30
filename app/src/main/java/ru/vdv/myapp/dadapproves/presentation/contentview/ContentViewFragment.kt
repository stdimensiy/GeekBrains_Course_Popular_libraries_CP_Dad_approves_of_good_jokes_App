package ru.vdv.myapp.dadapproves.presentation.contentview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.vdv.myapp.dadapproves.App
import ru.vdv.myapp.dadapproves.databinding.FragmentContentViewBinding
import ru.vdv.myapp.dadapproves.presentation.interfaces.BackButtonListener
import ru.vdv.myapp.dadapproves.presentation.interfaces.ContentView
import ru.vdv.myapp.mygitapiapp.myschedulers.MySchedulersFactory

class ContentViewFragment(modeView: Boolean, category: Int) : MvpAppCompatFragment(), ContentView,
    BackButtonListener {
    private var vb: FragmentContentViewBinding? = null
    private val presenter: ContentViewPresenter by moxyPresenter {
        ContentViewPresenter(
            modeView,
            category,
            MySchedulersFactory.create(),
            App.instance.router
        )
    }

    companion object {
        fun newInstance(moderationMode: Boolean, category: Int) =
            ContentViewFragment(moderationMode, category)
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
        vb?.btnBack?.setOnClickListener { presenter.btnBackPressed() }
        vb?.btnNext?.setOnClickListener { presenter.btnNextPressed() }
        hideErrorBar()
        hideProgressBar()
        showStatusNotVerified()
    }

    override fun moderatorModeInit() {
        vb?.btnApprove?.setOnClickListener { presenter.btnApprovePressed() }
        vb?.btnForbid?.setOnClickListener { presenter.btnForbidPressed() }
        vb?.etTag?.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    Log.d("Моя проверка", "Сработал мето сохранения метатега контента")
                    //попытка авторизации пользователя
                    presenter.saveTag(vb?.etTag?.editableText.toString())
                }
            }
            false
        }
    }

    override fun showProgressBar() {
        vb?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        vb?.progressBar?.visibility = View.GONE
    }

    override fun showErrorBar() {
        vb?.imgViewErrorSign?.visibility = View.VISIBLE
    }

    override fun hideErrorBar() {
        vb?.imgViewErrorSign?.visibility = View.GONE
    }

    override fun showModeratorBtnGroup() {
        vb?.groupBtnModerator?.visibility = View.VISIBLE
        vb?.groupStatModerator?.visibility = View.VISIBLE
        vb?.tvStatTitleTag?.visibility = View.GONE
    }

    override fun hideModeratorBtnGroup() {
        vb?.groupBtnModerator?.visibility = View.GONE
        vb?.groupStatModerator?.visibility = View.GONE
        vb?.tvStatTitleTag?.visibility = View.VISIBLE
    }

    override fun clearStatus() {
        vb?.tvNotVerifiedStatusContent?.visibility = View.GONE
        vb?.tvVerifiedApprovedStatusContent?.visibility = View.GONE
        vb?.tvVerifiedForbiddenStatusContent?.visibility = View.GONE
    }

    override fun showStatusNotVerified() {
        vb?.tvNotVerifiedStatusContent?.visibility = View.VISIBLE
    }

    override fun showStatusVerifiedAndApproved() {
        vb?.tvVerifiedApprovedStatusContent?.visibility = View.VISIBLE
    }

    override fun showStatusVerifiedAndForbidden() {
        vb?.tvVerifiedForbiddenStatusContent?.visibility = View.VISIBLE
    }

    override fun setTag(s: String) {
        vb?.etTag?.setText(s)
    }

    override fun setContent(s: String) {
        vb?.tvContent?.text = s
    }

    override fun backPressed(): Boolean = presenter.backPressed()
}