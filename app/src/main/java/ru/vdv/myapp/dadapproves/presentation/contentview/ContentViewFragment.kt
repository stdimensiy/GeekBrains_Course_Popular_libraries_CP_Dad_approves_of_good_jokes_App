package ru.vdv.myapp.dadapproves.presentation.contentview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.vdv.myapp.dadapproves.App
import ru.vdv.myapp.dadapproves.data.model.JokesRepository
import ru.vdv.myapp.dadapproves.data.retrofit.RNApiFactory
import ru.vdv.myapp.dadapproves.data.storage.MyStorageFactory
import ru.vdv.myapp.dadapproves.databinding.FragmentContentViewBinding
import ru.vdv.myapp.dadapproves.myschedulers.MySchedulersFactory
import ru.vdv.myapp.dadapproves.presentation.interfaces.BackButtonListener
import ru.vdv.myapp.dadapproves.presentation.interfaces.ContentView

class ContentViewFragment : MvpAppCompatFragment(), ContentView,
    BackButtonListener {
    private var vb: FragmentContentViewBinding? = null
    private val presenter: ContentViewPresenter by moxyPresenter {
        ContentViewPresenter(
            requireContext(),
            modeView == true,
            category,
            JokesRepository(RNApiFactory.create(), MyStorageFactory.create(requireContext())),
            MySchedulersFactory.create(),
            App.instance.router
        )
    }

    private val modeView: Boolean? by lazy {
        arguments?.getBoolean(DAD_MODE, false)
    }

    private val category: Int? by lazy {
        arguments?.getInt(ID_CATEGORY, 1)
    }

    companion object {
        private const val DAD_MODE = "DAD_MODE"
        private const val ID_CATEGORY = "ID_CATEGORY"
        fun newInstance(moderationMode: Boolean, category: Int) =
            ContentViewFragment().apply {
                arguments = bundleOf(DAD_MODE to moderationMode, ID_CATEGORY to category)
            }
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
        vb?.btnLoadNewJokeFromNetwork?.setOnClickListener { presenter.loadNewJokeFromNet() }
        vb?.etTag?.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
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
        vb?.btnLoadNewJokeFromNetwork?.visibility = View.VISIBLE
        vb?.tvStatTitleTag?.visibility = View.GONE
    }

    override fun hideModeratorBtnGroup() {
        vb?.groupBtnModerator?.visibility = View.GONE
        vb?.groupStatModerator?.visibility = View.GONE
        vb?.btnLoadNewJokeFromNetwork?.visibility = View.GONE
        vb?.tvStatTitleTag?.visibility = View.VISIBLE
    }

    override fun clearStatus() {
        vb?.tvNotVerifiedStatusContent?.visibility = View.GONE
        vb?.tvVerifiedApprovedStatusContent?.visibility = View.GONE
        vb?.tvVerifiedForbiddenStatusContent?.visibility = View.GONE
    }

    override fun enableBtnNext() {
        vb?.btnNext?.isEnabled = true
    }

    override fun enableBtnBack() {
        vb?.btnBack?.isEnabled = true
    }

    override fun disableBtnNext() {
        vb?.btnNext?.isEnabled = false
    }

    override fun disableBtnBack() {
        vb?.btnBack?.isEnabled = false
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

    override fun disableBtnApprove() {
        vb?.btnApprove?.isEnabled = false
    }

    override fun disableBtnForbidden() {
        vb?.btnForbid?.isEnabled = false
    }

    override fun enableBtnApprove() {
        vb?.btnApprove?.isEnabled = true
    }

    override fun enableBtnForbidden() {
        vb?.btnForbid?.isEnabled = true
    }

    override fun enableBtnLoadNewJokeFromNetwork() {
        vb?.btnLoadNewJokeFromNetwork?.isEnabled = true
    }

    override fun disableBtnLoadNewJokeFromNetwork() {
        vb?.btnLoadNewJokeFromNetwork?.isEnabled = false
    }

    override fun backPressed(): Boolean = presenter.backPressed()
}