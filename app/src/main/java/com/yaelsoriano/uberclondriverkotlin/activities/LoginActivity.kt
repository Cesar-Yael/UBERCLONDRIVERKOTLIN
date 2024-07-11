package com.yaelsoriano.uberclondriverkotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.yaelsoriano.uberclondriverkotlin.databinding.ActivityLoginBinding
import com.yaelsoriano.uberclondriverkotlin.providers.AuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authProvider = AuthProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.loginBtn.setOnClickListener { login() }
        binding.goToRegisterBtn.setOnClickListener { goToRegister() }
    }

    private fun login(){
        val email = binding.emailEditTxt.text.toString()
        val pass = binding.passEditTxt.text.toString()

        if (!isValidForm(email, pass)){
            Toast.makeText(this, "No puedes dejar ningún campo vacío", Toast.LENGTH_LONG).show()
            return
        }

        authProvider.login(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                goToMap()
                Toast.makeText(this, "Login exitoso!", Toast.LENGTH_LONG).show()
                Log.d("LoginActivity", "Login exitoso!")
            } else {
                Toast.makeText(this, "Algo salió mal(login).", Toast.LENGTH_LONG).show()
                Log.d("LoginActivity", "Algo salió mal(login): ${it.exception.toString()}")
            }
        }
    }

    private fun goToRegister(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun isValidForm(email: String, pass: String): Boolean {
        if (email.isEmpty() || pass.isEmpty()) {
            return false
        }
        return true
    }

    private fun goToMap() {
        val intent = Intent(this, MapActivity::class.java)
        intent. flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        if (authProvider.existSession()) {
            goToMap()
        }
    }
}