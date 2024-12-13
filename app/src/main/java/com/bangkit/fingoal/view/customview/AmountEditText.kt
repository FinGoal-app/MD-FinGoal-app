package com.bangkit.fingoal.view.customview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.bangkit.fingoal.R
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale

class AmountEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputEditText (context, attrs, defStyleAttr) {

    init {
        inputType = InputType.TYPE_CLASS_NUMBER
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    setError(context.getString(R.string.empty_name_warning), null)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                removeTextChangedListener(this)
                val cleanString = s.toString().replace("\\D".toRegex(), "")
                if (cleanString.isNotEmpty()) {
                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(parsed)
                    setText(formatted)
                    setSelection(formatted.length)
                } else {
                    setText("")
                }
                addTextChangedListener(this)
            }
        })
    }
}