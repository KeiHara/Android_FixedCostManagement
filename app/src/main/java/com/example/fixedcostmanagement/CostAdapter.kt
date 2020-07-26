package com.example.fixedcostmanagement

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlin.math.cos

class CostAdapter(context: Context, private val costs: OrderedRealmCollection<Cost>, val onItemClicked: (Cost) -> Unit):
    RealmRecyclerViewAdapter<Cost, CostAdapter.MyViewHolder>(costs, true) {

    // レイアウトビューからビューを生成するためのinflater
    private val inflater = LayoutInflater.from(context)

    // 表示する項目の数を取得
    override fun getItemCount(): Int {
        return costs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // viewの生成
        val view = inflater.inflate(R.layout.list_row, parent, false)
        // ビューホルダーの生成
        val viewHolder = MyViewHolder(view)
        // ビューをタップした時の処理
        view.setOnClickListener {
            // アダプター上の位置を取得
            val position = viewHolder.adapterPosition
            val cost = costs[position]
            onItemClicked(cost)
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cost = costs[position]
        holder.title.text = cost.title
        holder.cost.text = "¥ " + cost.cost.toString()
    }

    // ViewHolderの型を定義
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        // ビューホルダーの構成を定義
        val title = view.findViewById<TextView>(R.id.titleRow)
        val cost = view.findViewById<TextView>(R.id.costRow)
        // val content = view.findViewById<TextView>(R.id.)
    }
}