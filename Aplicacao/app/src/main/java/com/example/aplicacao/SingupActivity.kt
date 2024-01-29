package com.example.aplicacao

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacao.databinding.ActivitySingupBinding
import com.example.aplicacao.model.UserModel
import com.example.aplicacao.util.UiUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SingupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySingupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitBtn.setOnClickListener {
            singup()
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

    fun singup() {
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val confirmPassword = binding.confirmPasswordInput.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.setError("Email not valid")
            return
        }
        if (password.length < 6) {
            binding.passwordInput.setError("Minimus 6 character")
            return
        }
        if (password != confirmPassword) {
            binding.confirmPasswordInput.setError("Password not matched")
            return

        }
        singupWithFirebase(email, password)
    }

    fun singupWithFirebase(email: String, password: String) {
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            it.user?.let { user ->
                val userModel = UserModel(user.uid, email.substringBefore("@"))
                Firebase.firestore.collection("users")
                    .document(user.uid)
                    .set(userModel).addOnSuccessListener {
                        UiUtil.showToast(applicationContext, "Account created sucessfully")
                        setInProgress(false)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
            }
        }.addOnFailureListener {
            UiUtil.showToast(applicationContext, it.localizedMessage ?: "Something went wrong")
            setInProgress(false)
        }
    }
}