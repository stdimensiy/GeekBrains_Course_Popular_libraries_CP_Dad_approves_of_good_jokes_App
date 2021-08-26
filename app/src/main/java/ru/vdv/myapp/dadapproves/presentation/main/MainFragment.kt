package ru.vdv.myapp.dadapproves.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import ru.vdv.myapp.dadapproves.databinding.FragmentMainBinding

class MainFragment : MvpAppCompatFragment() {
    private var vb: FragmentMainBinding? = null

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
}