package com.example.aplicacao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.aplicacao.databinding.ActivityLoginBinding
import com.example.aplicacao.util.UiUtil
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.submitBtn.setOnClickListener {
            login()
        }

        binding.goToSignupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }

    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.submitBtn.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.submitBtn.visibility = View.VISIBLE
        }
    }

    fun login() {
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.setError("Email not valid")
            return
        }
        if (password.length < 6) {
            binding.passwordInput.setError("Minimus 6 character")
            return
        }
        loginWithFirebase(email, password)

    }

    fun loginWithFirebase(email: String, password: String) {
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            UiUtil.showToast(applicationContext, "Account created sucessfully")
            setInProgress(false)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.addOnFailureListener {
            UiUtil.showToast(applicationContext,it.localizedMessage?:"Something went wrhong")
            setInProgress(false)
        }
    }
}

