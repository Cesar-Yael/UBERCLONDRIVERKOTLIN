package com.yaelsoriano.uberclondriverkotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.yaelsoriano.uberclondriverkotlin.databinding.ActivityRegisterBinding
import com.yaelsoriano.uberclondriverkotlin.models.Client
import com.yaelsoriano.uberclondriverkotlin.providers.AuthProvider
import com.yaelsoriano.uberclondriverkotlin.providers.ClientProvider

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authProvider = AuthProvider()
    private val clientProvider = ClientProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.goToLoginBtn.setOnClickListener { goToLogin() }
        binding.registerBtn.setOnClickListener { register() }
    }

    private fun goToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun register(){
        val name = binding.nameEditTxt.text.toString()
        val lastname = binding.lastNameEditTxt.text.toString()
        val phone = binding.phoneNumberEditTxt.text.toString()
        val email = binding.emailEditTxt.text.toString()
        val pass = binding.passEditTxt.text.toString()
        val confirmPass = binding.confirmPassEditTxt.text.toString()

        if (!isValidForm(name, lastname, phone, email, pass, confirmPass)) { Toast.makeText(this, "No puedes dejar ningún campo vacío", Toast.LENGTH_LONG).show() }

        authProvider.register(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val client = Client(
                    id = authProvider.getId(),
                    name = name,
                    lastname = lastname,
                    phone = phone,
                    email = email
                )
                clientProvider.create(client).addOnCompleteListener {
                    if (it.isSuccessful) {
                        goToMap()
                        Toast.makeText(this, "Registro exitoso!", Toast.LENGTH_LONG).show()
                        Log.d("RegisterActivity", "Registro exitoso!")
                    } else {
                        Toast.makeText(this, "Algo salió mal(fire-store).", Toast.LENGTH_LONG).show()
                        Log.d("RegisterActivity", "Algo salió mal(fire-store): ${it.exception.toString()}")
                    }
                }

            } else {
                Toast.makeText(this, "Algo salió mal(fire-auth).", Toast.LENGTH_LONG).show()
                Log.d("RegisterActivity", "Algo salió mal(fire-auth): ${it.exception.toString()}")
            }
        }
    }

    private fun isValidForm(name: String, lastName: String, phoneNumber: String, email: String, pass: String, confirmPass: String): Boolean {
        if (name.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            return false
        }

        if (pass.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show()
            return false
        }

        if (pass != confirmPass) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun goToMap() {
        val intent = Intent(this, MapActivity::class.java)
        intent. flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}