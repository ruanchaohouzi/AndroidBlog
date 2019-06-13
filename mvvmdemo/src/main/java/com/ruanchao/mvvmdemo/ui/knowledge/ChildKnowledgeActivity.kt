package com.ruanchao.mvvmdemo.ui.knowledge

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ruanchao.mvvmdemo.R

class ChildKnowledgeActivity : AppCompatActivity() {

    companion object {
        val KEY_CID = "cid"

        fun start(context: Context, cid:Int?){
            var intent = Intent(context, ChildKnowledgeActivity::class.java)
            intent.putExtra(KEY_CID, cid)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_knowledge)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_content,KnowledgeChildListFragment.newInstance(intent.getIntExtra(KEY_CID, 60)))
            .commit()

    }
}
