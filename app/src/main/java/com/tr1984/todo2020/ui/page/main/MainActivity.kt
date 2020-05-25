package com.tr1984.todo2020.ui.page.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.todo2020.R
import com.tr1984.todo2020.databinding.ActivityMainBinding
import com.tr1984.todo2020.ui.BaseActivity
import com.tr1984.todo2020.ui.page.detail.DetailActivity

class MainActivity : BaseActivity<MainViewModel>() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            activity = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }
        setContentView(binding.root)

        binding.recyclerView.adapter = MainAdapter(this, viewModel)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        binding.addMemo.show()
                    }
                    else -> {
                        binding.addMemo.hide()
                    }
                }
            }
        })
        binding.addMemo.setOnClickListener {
            startDetailActivity()
        }

        viewModel.checkExpired()
    }

    override fun observeViewModel() {
        super.observeViewModel()
        viewModel.selectedTodo.observe(this, Observer {
            startDetailActivity(true, it.entity.id)
        })

        viewModel.expiredTodos.observe(this, Observer {
            if (it.isEmpty()) {
                return@Observer
            }
            val titles = it.map { it.entity.title }
            AlertDialog.Builder(this@MainActivity)
                .setTitle(getString(R.string.main_expired_memo_list))
                .setAdapter(
                    ArrayAdapter<String>(
                        this@MainActivity,
                        android.R.layout.select_dialog_item,
                        titles
                    )
                ) { dialog, position ->
                    val todoId = it[position].entity.id
                    startDetailActivity(true, todoId)
                    dialog.dismiss()
                }
                .setPositiveButton(
                    getString(R.string.submit)
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        })
    }

    private fun startDetailActivity(isEdit: Boolean = false, todoId: Long = 0) {
        startActivity(Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_EDIT, isEdit)
            putExtra(DetailActivity.EXTRA_TODO_ID, todoId)
        })
    }
}
