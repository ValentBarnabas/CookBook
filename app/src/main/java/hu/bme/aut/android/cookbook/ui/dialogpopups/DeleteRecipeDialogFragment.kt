package hu.bme.aut.android.cookbook.ui.dialogpopups

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import hu.bme.aut.android.cookbook.R
import java.lang.ClassCastException

class DeleteRecipeDialogFragment : AppCompatDialogFragment() {

    private var resultDialogListener: ResultDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder = AlertDialog.Builder(targetFragment?.requireContext()!!)
        var layoutInflater = requireActivity().layoutInflater
        var view = layoutInflater.inflate(R.layout.fragment_dialogpopup, null)

        builder.setView(view).setTitle(arguments?.getString("text"))
            .setNegativeButton(R.string.dialog_popup_negativeOption, DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int -> })
            .setPositiveButton(R.string.dialog_popup_positiveOption, DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                resultDialogListener?.returnValue(true, tag.toString())
            })
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            resultDialogListener = (targetFragment as ResultDialogListener)
        } catch (e: ClassCastException) {
            ClassCastException(context.toString() + "must implement ResultDialogListener")
        }
    }

    interface ResultDialogListener {
        fun returnValue(bool: Boolean, tag: String)
    }
}