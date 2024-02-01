package com.example.aplicacao

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.aplicacao.databinding.ActivityVideoUploadBinding
import com.example.aplicacao.util.UiUtil

class VideoUploadActivity : AppCompatActivity() {

    lateinit var binding: ActivityVideoUploadBinding
    private var selectedVideoUri: Uri? = null
    lateinit var videoLaucher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_upload)

        videoLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (
                    result.resultCode == RESULT_OK
                ) {
                    selectedVideoUri = result.data?.data
                    UiUtil.showToast(this,"Got Video" +selectedVideoUri.toString())
                }
            }
        binding.uploadView.setOnClickListener {
            checkPermissionAndOpenVideoPicker() {
            }
        }


    }
    private fun checkPermissionAndOpenVideoPicker(function: () -> Unit) {
        var readExternalVideo: String = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            readExternalVideo = android.Manifest.permission.READ_MEDIA_VIDEO
        } else {
            readExternalVideo = android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(
                this,
                readExternalVideo
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openVideoPicker()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(readExternalVideo),
                100
            )
        }
    }


    private fun openVideoPicker() {
        var inten = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        videoLaucher.launch(intent)
    }
}