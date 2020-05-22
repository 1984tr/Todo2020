package com.tr1984.todo2020.ui.page.detail

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tr1984.todo2020.R
import com.tr1984.todo2020.databinding.ActivityDetailBinding
import com.tr1984.todo2020.extensions.alert
import com.tr1984.todo2020.ui.BaseActivity
import com.tr1984.todo2020.ui.BaseViewModel
import java.util.*

class DetailActivity : BaseActivity<DetailViewModel>() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@DetailActivity
            activity = this@DetailActivity
            viewModel = this@DetailActivity.viewModel
        }
        setContentView(binding.root)

        supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        val todoId = intent.getLongExtra(EXTRA_TODO_ID, 0L)
        viewModel.fetch(todoId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val isEdit = intent.getBooleanExtra(EXTRA_EDIT, false)
        if (isEdit) {
            menuInflater.inflate(R.menu.detail_menu_edit, menu)
        } else {
            menuInflater.inflate(R.menu.detail_menu_create, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_submit -> submit()
            R.id.item_delete -> delete()
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker() {
        val cal = Calendar.getInstance()
        val dialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.onDateSet(year, month, dayOfMonth)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)
        )
        if (!isFinishing) {
            dialog.show()
        }
    }

    private fun submit(): Boolean {
        viewModel.submit {
            setResult(Activity.RESULT_OK)
            finish()
        }
        return true
    }

    private fun delete(): Boolean {
        alert(getString(R.string.notify), getString(R.string.detail_are_you_delete_memo), BaseViewModel.Notifier.Alert.Button(getString(
                    R.string.yes)) {
            viewModel.delete {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }, BaseViewModel.Notifier.Alert.Button(getString(R.string.no)))
        return true
    }

    companion object {
        const val EXTRA_EDIT = "isEdit"
        const val EXTRA_TODO_ID = "todoId"
    }
}