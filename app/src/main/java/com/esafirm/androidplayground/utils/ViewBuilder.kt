package com.esafirm.androidplayground.utils

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.esafirm.androidplayground.common.MenuFactory

enum class ScrollDirection {
    HORIZONTAL,
    VERTICAL
}

interface ContextProvider {
    val requiredContext: Context
}

private fun linearLayout(context: Context, orientation: Int): LinearLayout {
    return LinearLayout(context).apply {
        this.orientation = orientation
        matchParent()
    }
}

fun ContextProvider.scroll(
    direction: ScrollDirection = ScrollDirection.VERTICAL,
    block: ViewGroup.() -> Unit
): ScrollView {
    val scrollView = ScrollView(requiredContext).apply {
        matchParent()
    }
    val item = when (direction) {
        ScrollDirection.HORIZONTAL -> column(block)
        ScrollDirection.VERTICAL -> row(block)
    }
    scrollView.addView(item)
    return scrollView
}

fun ContextProvider.row(block: ViewGroup.() -> Unit): LinearLayout {
    return linearLayout(requiredContext, LinearLayout.VERTICAL).also(block)
}

fun ContextProvider.column(block: ViewGroup.() -> Unit): LinearLayout {
    return linearLayout(requiredContext, LinearLayout.HORIZONTAL).also(block)
}

fun ViewGroup.input(placeholder: String = "", onTextChange: (String) -> Unit): View {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChange(s?.toString() ?: "")
        }
    }

    val view = EditText(context).apply {
        hint = placeholder
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                removeTextChangedListener(textWatcher)
            }

            override fun onViewAttachedToWindow(v: View?) {
                addTextChangedListener(textWatcher)
            }
        })
        matchParent(horizontal = true)
    }
    addView(view)

    onTextChange("")

    return view
}

fun ViewGroup.button(text: String, onClick: () -> Unit) =
    addView(Button(context).apply {
        this.text = text
        setOnClickListener { onClick.invoke() }
        matchParent(horizontal = true)
    })

fun ViewGroup.matchParent(horizontal: Boolean = true, vertical: Boolean = true) {
    layoutParams = ViewGroup.LayoutParams(
        if (horizontal) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT,
        if (vertical) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun ViewGroup.logger() = addView(Logger.getLogView(context))

fun ViewGroup.menu(list: List<MenuFactory.NavigateItem>): View {
    return MenuFactory.render(this, list)
}

fun ViewGroup.webView(): WebView {
    val webView = WebView(context).apply {
        setBackgroundColor(Color.LTGRAY)
    }
    addView(webView)
    return webView
}

fun ViewGroup.image(
    imageUrl: String,
    @DrawableRes placeholder: Int? = null
) {
    addView(ImageView(context).apply {
        matchParent(vertical = false)

        Glide.with(this)
            .load(imageUrl).apply {
                if (placeholder != null) {
                    placeholder(placeholder)
                }
            }
            .into(this)

    })
}