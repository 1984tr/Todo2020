package com.tr1984.todo2020.ui.page.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.todo2020.R
import com.tr1984.todo2020.databinding.ItemTodoBinding
import com.tr1984.todo2020.model.Todo

class MainAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel
) :
    ListAdapter<Todo, MainAdapter.Holder>(diffCallback) {

    init {
        viewModel.items.observe(lifecycleOwner, Observer {
            submitList(it)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(DataBindingUtil.inflate(inflater, R.layout.item_todo, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            lifecycleOwner = this@MainAdapter.lifecycleOwner
            viewModel = this@MainAdapter.viewModel
            data = getItem(position)
        }
    }

    class Holder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback by lazy {
            object :
                DiffUtil.ItemCallback<Todo>() {

                override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                    return oldItem.entity.id == newItem.entity.id
                }

                override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                    return oldItem.equals(newItem)
                }
            }
        }
    }
}