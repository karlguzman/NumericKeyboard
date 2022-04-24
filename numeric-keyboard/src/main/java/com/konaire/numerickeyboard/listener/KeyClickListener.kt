package com.konaire.numerickeyboard.listener

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView

import com.konaire.numerickeyboard.view.IconifiedTextView
import kotlin.math.min

internal class KeyClickListener(
    private val field: EditText?,
    private val maxLength: Int
) : View.OnClickListener {
    override fun onClick(view: View?) {
        val field = this.field ?: return

        if (view is TextView) {
            val keyCode = Integer.parseInt(view.tag as String)
            field.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
        }
    }

    private fun EditText.addCharAfterSelection(newChar: Char) {
        if (isBiggerThanMaxLength(text)) {
            return
        }

        val selectionEnd = this.selectionEnd
        val newTextBuilder = StringBuilder()
            .append(text.subSequence(0, selectionEnd))
            .append(newChar)
            .append(text.subSequence(selectionEnd, length()))

        setText(newTextBuilder)
        setSelectionWithValidation(selectionEnd + 1) // set selection to the next character
    }

    private fun isBiggerThanMaxLength(
        text: CharSequence
    ) = maxLength > 0 && text.length >= maxLength

    private fun EditText.removeCharBeforeSelection() {
        val removableCharPosition = selectionEnd - 1
        if (removableCharPosition < 0) {
            return // do nothing if there are no characters before the cursor
        }

        val newTextBuilder = StringBuilder()
            .append(text.substring(0, removableCharPosition))
            .append(text.substring(removableCharPosition + 1))

        setText(newTextBuilder)
        setSelectionWithValidation(removableCharPosition)
    }

    private fun EditText.setSelectionWithValidation(index: Int) {
        setSelection(min(index, text.length))
    }
}
