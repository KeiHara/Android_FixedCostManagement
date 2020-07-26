package com.example.fixedcostmanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    // Realmクラスのプロパティを準備
    // onCreate内で初期化をするためにlateinit修飾子をつけた
    private lateinit var realm: Realm

    // 表示単位を切り替えるための変数
    var flag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Realクラスのインスタンスを取得
        realm = Realm.getDefaultInstance()
        val costs = realm.where<Cost>().findAll()
        // 合計金額を取得
        var total = costs.sum("cost").toString()
        total = String.format("%,d", total.toInt())
        // 合計金額をトップ画面に設定
        totalCost.text = total

        // 作成したリストビューにアダプターを設定する
        val recyclerView = costList
        val adapter = CostAdapter(this, costs) { cost ->
            startActivity<EditActivity>("cost_id" to cost.id)
        }

        recyclerView.adapter = adapter
        // recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val layout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layout.stackFromEnd = true
        recyclerView.layoutManager = layout

        fab.setOnClickListener {
            startActivity<EditActivity>()
        }

        unitButton.setOnClickListener {
            realm = Realm.getDefaultInstance()
            val costs = realm.where<Cost>().findAll()
            // 合計金額を取得
            var total = costs.sum("cost").toString()
            if (flag == 0) {
                flag = 1
                var annual = (total.toInt() * 12).toString()
                annual = String.format("%,d", annual.toInt())
                totalCost.text = annual
                totalText.text = "総費用　¥/年"
            } else {
                flag = 0
                total = String.format("%,d", total.toInt())
                totalCost.text = total
                totalText.text = "総費用　¥/月"
            }
        }
    }

    // edit activityでfinish()が呼び出されてmain activityに遷移した際、総費用の表示を後進するためのコールバック関数
    override fun onStart() {
        super.onStart()
        // Realクラスのインスタンスを取得
        realm = Realm.getDefaultInstance()
        val costs = realm.where<Cost>().findAll()
        // 合計金額を取得
        var total = costs.sum("cost").toString()
        total = String.format("%,d", total.toInt())
        // 合計金額をトップ画面に設定
        totalCost.text = total
        flag = 0
        totalText.text = "総費用　¥/月"
    }

    // アクティビティの終了処理
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
