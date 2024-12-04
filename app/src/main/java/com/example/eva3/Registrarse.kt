package com.example.eva3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eva3.databinding.RegistrarseBinding
import com.google.firebase.auth.FirebaseAuth

class Registrarse : AppCompatActivity() {

    private lateinit var binding: RegistrarseBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistrarseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            val intent = Intent(this, IniciarSesion::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.etEmail.error = "Por favor ingresa un correo"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Por favor ingresa una contraseña"
                return@setOnClickListener
            }

            registerUser(email, password)
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, IniciarSesion::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, IniciarSesion::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
