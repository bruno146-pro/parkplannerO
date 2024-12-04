package com.example.eva3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eva3.Models.Reserva
import com.example.eva3.databinding.ActivityEditarReservaBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditarReservaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarReservaBinding
    private lateinit var database: DatabaseReference
    private lateinit var reserva: Reserva

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarReservaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar el objeto reserva desde el Intent
        reserva = intent.getSerializableExtra("reserva") as Reserva

        // Configurar la base de datos de Firebase
        database = FirebaseDatabase.getInstance().getReference("Reservas")

        // Rellenar los campos con los datos actuales de la reserva
        binding.etNombreReserva.setText(reserva.nombre)
        binding.etPantete.setText(reserva.patente)

        // Configurar el botón de guardar
        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombreReserva.text.toString()
            val patente = binding.etPantete.text.toString()

            // Validar los campos
            if (nombre.isEmpty() || patente.isEmpty()) {
                Snackbar.make(binding.root, "Los campos no pueden estar vacíos", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Actualizar los valores en el objeto reserva
            reserva.nombre = nombre
            reserva.patente = patente

            // Actualizar la reserva en Firebase
            database.child(reserva.id!!).setValue(reserva).addOnSuccessListener {
                Snackbar.make(binding.root, "Reserva Actualizada", Snackbar.LENGTH_SHORT).show()
                finish()  // Volver a la actividad anterior
            }.addOnFailureListener {
                Snackbar.make(binding.root, "Error al actualizar la reserva", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
