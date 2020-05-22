package com.tr1984.todo2020.ui.dialog

import android.app.Dialog
import android.content.Context
import java.util.concurrent.atomic.AtomicInteger

class LoadingDialog(context: Context) : Dialog(context) {

    private var count = AtomicInteger(0)

    override fun show() {
        if (isShowing) {
            count.incrementAndGet()
            return
        }
        super.show()
    }

    override fun dismiss() {
        if (count.get() == 0) {
            super.dismiss()
        } else {
            count.decrementAndGet()
        }
    }
}