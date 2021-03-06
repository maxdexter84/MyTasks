package ru.maxdexter.mytasks.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

class TakeImageContract: ActivityResultContract<Uri,Pair<Uri,String>?>(){
    var uri: Uri = Uri.EMPTY
    override fun createIntent(context: Context, input: Uri): Intent {
        uri = input
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Uri,String> {
        var type = ""
        var picUri = Uri.EMPTY
        if (resultCode == Activity.RESULT_OK ) {
                type = "image"
                picUri = uri

       }
        return Pair(picUri,type)
    }


}