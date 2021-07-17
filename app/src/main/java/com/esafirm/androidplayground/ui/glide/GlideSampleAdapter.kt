package com.esafirm.androidplayground.ui.glide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.esafirm.androidplayground.R

data class GlideItem(
    val isAvatar: Boolean,
    val imageUrl: String
)

internal abstract class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract val imageView: ImageView
}

internal class GlideSampleAdapter(
) : RecyclerView.Adapter<ImageViewHolder>() {

    companion object {
        private const val TYPE_AVATAR = 1
        private const val TYPE_CONTENT = 2
    }

    private var items: List<GlideItem> = emptyList()
    private var useOnPreDraw: Boolean = false

    fun setup(
        items: List<GlideItem>,
        useOnPreDraw: Boolean = false
    ) {
        this.items = items
        this.useOnPreDraw = useOnPreDraw
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == TYPE_AVATAR) {
            return AvatarViewHolder(inflater.inflate(R.layout.item_glide_avatar, parent, false))
        }
        return ContentViewHolder(inflater.inflate(R.layout.item_glide_content, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isAvatar) TYPE_AVATAR else TYPE_CONTENT
    }

    override fun getItemCount(): Int = items.size

    class AvatarViewHolder(itemView: View) : ImageViewHolder(itemView) {
        override val imageView: ImageView
            get() = itemView.findViewById(R.id.imgAvatar)
    }

    class ContentViewHolder(itemView: View) : ImageViewHolder(itemView) {
        override val imageView: ImageView
            get() = itemView.findViewById(R.id.imgContent)

    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (useOnPreDraw) {
            holder.imageView.requestImageWithMeasure()
        } else {
            holder.imageView.requestImage()
        }
    }
}
