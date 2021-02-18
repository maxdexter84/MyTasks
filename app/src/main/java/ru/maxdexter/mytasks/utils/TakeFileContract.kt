package ru.maxdexter.mytasks.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract


class TakeFileContract: ActivityResultContract<Array<String>, Pair<Uri,String>>() {
   lateinit var con: Context
    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Uri,String>? {
      return  if (intent != null && resultCode == Activity.RESULT_OK ){
            Pair(intent.data ?: Uri.EMPTY, intent.resolveType(con.contentResolver) ?: "" )

        }else null
    }


    override fun createIntent(context: Context, input: Array<String>?): Intent {
        con = context
        return Intent(Intent.ACTION_OPEN_DOCUMENT)
            .putExtra(Intent.EXTRA_MIME_TYPES, input)
            .setType("*/*")
    }


}