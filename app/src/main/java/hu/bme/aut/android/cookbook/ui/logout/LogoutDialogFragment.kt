package hu.bme.aut.android.cookbook.ui.logout

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import hu.bme.aut.android.cookbook.R
import java.lang.ClassCastException

class LogoutDialogFragment : AppCompatDialogFragment() {

    private lateinit var logoutViewModel: LogoutViewModel

    private var resultDialogListener:ResultDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder = AlertDialog.Builder(requireContext())
        var layoutInflater = requireActivity().layoutInflater
        var view = layoutInflater.inflate(R.layout.fragment_logoutdialog, null)

        builder.setView(view).setTitle(R.string.fragment_logoutdialog_tvText)
            .setNegativeButton(R.string.fragment_logoutdialog_negativeOption, DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int -> })
            .setPositiveButton(R.string.fragment_logoutdialog_positiveOption, DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                    resultDialogListener?.returnValue(true)
            })
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            resultDialogListener = (context as ResultDialogListener)
        } catch (e: ClassCastException) {
            ClassCastException(context.toString() + "must implement ResultDialogListener")
        }
    }

    interface ResultDialogListener {
        fun returnValue(bool: Boolean)
    }
}