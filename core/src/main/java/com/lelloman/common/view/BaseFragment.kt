package com.lelloman.common.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.lelloman.common.viewmodel.BaseViewModel
import com.lelloman.common.viewmodel.command.Command
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

    protected abstract val viewModel: VM

    protected abstract val layoutResId: Int

    private lateinit var commandsSubscription: Disposable

    protected lateinit var binding: DB

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val baseActivity = context as? BaseActivity<*, *>
            ?: error("BaseFragment must be attached to a BaseActivity")

        commandsSubscription = viewModel
            .commands
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!handleCommand(it)) {
                    baseActivity.handleCommand(it)
                }
            }
    }

    protected open fun handleCommand(command: Command) = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        setViewModel(binding, viewModel)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        commandsSubscription.dispose()
    }

    protected abstract fun setViewModel(binding: DB, viewModel: VM)
}