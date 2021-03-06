package com.simplemobiletools.commons.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.simplemobiletools.commons.R
import com.simplemobiletools.commons.extensions.*

/**
 * A dialog for choosing between internal, root, SD card (optional) storage
 *
 * @param context has to be activity context to avoid some Theme.AppCompat issues
 * @param currPath current path to decide which storage should be preselected
 * @param callback an anonymous function
 *
 */
class StoragePickerDialog(val context: Context, currPath: String, val callback: (pickedPath: String) -> Unit) {
    var mDialog: AlertDialog?

    init {
        val inflater = LayoutInflater.from(context)
        val resources = context.resources
        val basePath = currPath.getBasePath(context)
        val layoutParams = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val radioGroup = inflater.inflate(R.layout.radio_group, null) as RadioGroup

        val internalButton = inflater.inflate(R.layout.radio_button, null) as RadioButton
        internalButton.apply {
            text = resources.getString(R.string.internal)
            isChecked = basePath == context.getInternalStoragePath()
            setOnClickListener { internalPicked() }
        }
        radioGroup.addView(internalButton, layoutParams)

        if (context.hasExternalSDCard()) {
            val sdButton = inflater.inflate(R.layout.radio_button, null) as RadioButton
            sdButton.apply {
                text = resources.getString(R.string.sd_card)
                isChecked = basePath == context.getSDCardPath()
                setOnClickListener { sdPicked() }
            }
            radioGroup.addView(sdButton, layoutParams)
        }

        val rootButton = inflater.inflate(R.layout.radio_button, null) as RadioButton
        rootButton.apply {
            text = resources.getString(R.string.root)
            isChecked = basePath == "/"
            setOnClickListener { rootPicked() }
        }
        radioGroup.addView(rootButton, layoutParams)

        mDialog = AlertDialog.Builder(context)
                .create().apply {
            context.setupDialogStuff(radioGroup, this, R.string.select_storage)
        }
    }

    private fun internalPicked() {
        mDialog?.dismiss()
        callback.invoke(context.getInternalStoragePath())
    }

    private fun sdPicked() {
        mDialog?.dismiss()
        callback.invoke(context.getSDCardPath())
    }

    private fun rootPicked() {
        mDialog?.dismiss()
        callback.invoke("/")
    }
}
