package com.example.aplicacao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplicacao.databinding.ActivityMainBinding
import com.example.aplicacao.util.UiUtil

lateinit var binding : ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavBar.setOnItemSelectedListener{menuItem->
            when(menuItem.itemId) {
                R.id.bottom_menu_home->{
                    UiUtil.showToast(this,"Add video")
                }
                R.id.bottom_menu_add_video->{
                    UiUtil.showToast(this,"Add video")
                }
                R.id.bottom_menu_profile->{
                    UiUtil.showToast(this,"Add video")
                }
            }
            false
        }

    }
}