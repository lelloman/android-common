package com.lelloman.common.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.lelloman.common.data.model.ModelWithId
import com.lelloman.common.utils.ModelWithIdListDiffCalculator
import com.lelloman.common.viewmodel.BaseListItemViewModel

abstract class BaseRecyclerViewAdapter<ID, M : ModelWithId<ID>, VM : BaseListItemViewModel<ID, M>, DB : ViewDataBinding>(
    private val onItemClickListener: (M) -> Unit = {}
) : RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<ID, M, VM, DB>>(), Observer<List<M>> {

    private var data = emptyList<M>()
    private val listDiffCalculator = ModelWithIdListDiffCalculator<ID>()

    abstract val listItemLayoutResId: Int

    abstract fun bindViewModel(binding: DB, viewModel: VM)

    protected abstract fun createViewModel(viewHolder: BaseViewHolder<ID, M, VM, DB>): VM

    fun getItem(position: Int) = data[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ID, M, VM, DB> {
        val binding: DB = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            listItemLayoutResId,
            parent,
            false
        )

        return BaseViewHolder(
            binding = binding,
            viewModelFactory = ::createViewModel,
            onClickListener = onItemClickListener,
            viewModelBinder = ::bindViewModel
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseViewHolder<ID, M, VM, DB>, position: Int) = holder.bind(data[position])

    override fun onChanged(newData: List<M>?) {
        newData?.let {
            val diff = listDiffCalculator.computeDiff(data, newData)
            this.data = newData
            diff.dispatchUpdatesTo(this)
        }
    }

    class BaseViewHolder<ID, M : ModelWithId<ID>, VM : BaseListItemViewModel<ID, M>, DB : ViewDataBinding>(
        private val binding: DB,
        private val viewModelBinder: (DB, VM) -> Unit,
        viewModelFactory: (BaseViewHolder<ID, M, VM, DB>) -> VM,
        onClickListener: ((M) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        @Suppress("MemberVisibilityCanBePrivate")
        lateinit var item: M
        val viewModel: VM = viewModelFactory.invoke(this)

        init {
            if (onClickListener != null) {
                binding.root.setOnClickListener { onClickListener(item) }
            }
        }

        fun bind(item: M) {
            this.item = item
            viewModel.bind(item)
            viewModelBinder(binding, viewModel)
            binding.executePendingBindings()
        }
    }
}