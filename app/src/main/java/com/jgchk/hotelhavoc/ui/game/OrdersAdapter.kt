package com.jgchk.hotelhavoc.ui.game

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.core.extension.inflate
import kotlinx.android.synthetic.main.order_item_layout.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class OrdersAdapter
@Inject constructor() : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    internal var collection: List<OrderView> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.order_item_layout))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(collection[position])

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(orderView: OrderView) {
            itemView.order_view.setImageDrawable(makeOrderDrawable(orderView))
            itemView.turn_in_room_view.text = orderView.turnInRoom.toString()
        }

        private fun makeOrderDrawable(orderView: OrderView): Drawable {
            val layerDrawable =
                LayerDrawable(orderView.drawables.map { itemView.context.resources.getDrawable(it) }.toTypedArray())
            for (i in 0 until layerDrawable.numberOfLayers) {
                layerDrawable.setLayerInset(i, 0, (layerDrawable.numberOfLayers - i) * 200, 0, i * 200)
            }
            return layerDrawable
        }
    }
}