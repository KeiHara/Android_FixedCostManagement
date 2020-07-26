package com.example.fixedcostmanagement

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Cost: RealmObject() {
    @PrimaryKey
    var id: Long = 0 // 主キー
    var title: String = "" // タイトル
    var cost: Long = 0 // コスト
    var content: String = "" //備考
}