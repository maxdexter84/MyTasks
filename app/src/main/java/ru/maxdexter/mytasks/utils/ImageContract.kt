package ru.maxdexter.mytasks.utils

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import java.net.URI

class ImageContract: ActivityResultContract<URI, URI>() {
    override fun createIntent(context: Context, input: URI?): Intent {
        return Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): URI {
        TODO("Not yet implemented")
    }
}