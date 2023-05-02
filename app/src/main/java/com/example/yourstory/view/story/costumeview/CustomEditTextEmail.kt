package com.example.yourstory.view.story.costumeview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet

class CustomEditTextEmail : androidx.appcompat.widget.AppCompatEditText {

    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var email = ""

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        // set some initial properties for the view
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.toString().matches(emailPattern.toRegex())) {
                    error = "Email tidak valid"
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

}