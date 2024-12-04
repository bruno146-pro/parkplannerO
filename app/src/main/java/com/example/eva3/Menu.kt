package com.example.eva3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eva3.databinding.MenuBinding
import com.google.firebase.auth.FirebaseAuth

class Menu : AppCompatActivity() {

    private lateinit var binding: MenuBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnEditarPerfil.setOnClickListener {
            Toast.makeText(this, "Editar perfil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EditarPerfil::class.java)
            startActivity(intent)
        }

        binding.btnCrearReservas.setOnClickListener {
            Toast.makeText(this, "Crear Reservas", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnverReservasDisponibles.setOnClickListener {
            Toast.makeText(this, "Ver reservas disponibles", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, VerReservasActivity::class.java)
            startActivity(intent)
        }

        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, IniciarSesion::class.java)
            startActivity(intent)
            finish()
        }
    }
}
