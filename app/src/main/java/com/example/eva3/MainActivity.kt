package com.example.eva3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eva3.Models.Reserva
import com.example.eva3.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = FirebaseDatabase.getInstance().getReference("Reservas")

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombreReserva.text.toString()
            val patente = binding.etPantete.text.toString()

            if (nombre.isEmpty()) {
                binding.etNombreReserva.error = "Por favor Ingresa nombre"
                return@setOnClickListener
            }
            if (patente.isEmpty()) {
                binding.etPantete.error = "Por favor Ingresa Patente"
                return@setOnClickListener
            }

            // Obtener la cantidad actual de reservas
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentSize = snapshot.childrenCount.toInt()

                    if (currentSize < 16) {
                        // Si hay menos de 16 reservas, se puede agregar una nueva
                        val id = database.push().key ?: return
                        val reserva = Reserva(id, nombre, patente)

                        database.child(id).setValue(reserva).addOnSuccessListener {
                            binding.etNombreReserva.setText("")
                            binding.etPantete.setText("")
                            Snackbar.make(binding.root, "Reserva Agregada", Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        // Si ya hay 16 reservas, mostrar un mensaje de error
                        Snackbar.make(
                            binding.root,
                            "Se ha alcanzado el límite de 16 reservas",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejo de errores si ocurre un fallo
                    Snackbar.make(
                        binding.root,
                        "Error al verificar las reservas",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
        }
        binding.btnVer.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
    }
}
