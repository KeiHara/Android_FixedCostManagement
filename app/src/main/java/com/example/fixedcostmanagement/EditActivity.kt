package com.example.fixedcostmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class EditActivity : AppCompatActivity() {

    // Realmクラスのプロパティを準備
    // onCreate内で初期化をするためにlateinit修飾子をつけた
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        realm = Realm.getDefaultInstance()

        // MainActivityでリストのセルを選択してEditActivityに遷移した時の処理
        val costId = intent.getLongExtra("cost_id", -1L)
        if (costId != -1L) {
            val cost = realm.where<Cost>().equalTo("id", costId).findFirst()
            titleEdit.setText(cost?.title ?: "")
            costEdit.setText(cost?.cost.toString())
            contentEdit.setText(cost?.content ?: "")
            delete.visibility = View.VISIBLE
        } else {
            delete.visibility = View.INVISIBLE
        }

        save.setOnClickListener {
            // タイトルと本文が入力されているかを確認するis_valid
            var isValid = true
            when (costId) {
                -1L -> {
                    // タイトルの入力確認　空欄ならisValidをfalse
                    if (titleEdit.text.isEmpty()) {
                        titleEdit.error = "タイトルを入力してください"
                        isValid = false
                    }
                    // コストの入力確認　空欄ならisValidをfalse
                    if (costEdit.text.isEmpty()) {
                        costEdit.error = "コストを入力してください"
                        isValid = false
                    }
                    if (isValid) {
                        realm.executeTransaction {
                            val maxId = realm.where<Cost>().max("id")
                            val nextId = (maxId?.toLong() ?: 0L) + 1
                            val cost = realm.createObject<Cost>(nextId)
                            cost.title = titleEdit.text.toString()
                            val costString = costEdit.text.toString()
                            cost.cost = costString.toLong()
                            cost.content = contentEdit.text.toString()
                        }
                        alert("追加しました") {
                            yesButton { finish() }
                        }.show()
                    }
                }
                else -> {
                    // タイトルの入力確認　空欄ならisValidをfalse
                    if (titleEdit.text.isEmpty()) {
                        titleEdit.error = "タイトルを入力してください"
                        isValid = false
                    }
                    // コストの入力確認　空欄ならisValidをfalse
                    if (costEdit.text.isEmpty()) {
                        costEdit.error = "コストを入力してください"
                        isValid = false
                    }
                    if (isValid) {
                        realm.executeTransaction {
                            val cost = realm.where<Cost>().equalTo("id", costId).findFirst()
                            cost?.title = titleEdit.text.toString()
                            val costString = costEdit.text.toString()
                            cost?.cost = costString.toLong()
                            cost?.content = contentEdit.text.toString()
                        }
                        alert("編集しました") {
                            yesButton { finish() }
                        }.show()
                    }
                }
            }
        }

        delete.setOnClickListener {
            realm.executeTransaction {
                val cost = realm.where<Cost>().equalTo("id", costId)?.findFirst()
                cost?.deleteFromRealm()
            }
            alert("削除しますか？") {
                yesButton { finish() }
                noButton {}
            }.show()
        }
    }
}
