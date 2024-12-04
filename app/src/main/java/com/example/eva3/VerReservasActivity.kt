package com.example.eva3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eva3.Adapter.AdapterReserva
import com.example.eva3.Models.Reserva
import com.example.eva3.databinding.ActivityVerReservasBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerReservasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerReservasBinding
    private lateinit var database: DatabaseReference
    private lateinit var reservasList: ArrayList<Reserva>
    private lateinit var adapterReserva: AdapterReserva
    private lateinit var reservaRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerReservasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reservaRecyclerView = binding.rvReservas
        reservaRecyclerView.layoutManager = LinearLayoutManager(this)
        reservaRecyclerView.hasFixedSize()

        reservasList = arrayListOf()

        // Obtener las reservas de Firebase
        getReservas()

        // Configurar el adaptador con los eventos de editar y eliminar
        adapterReserva = AdapterReserva(reservasList, { reserva ->
            // Acción de editar
            editarReserva(reserva)
        }, { reserva ->
            // Acción de eliminar
            eliminarReserva(reserva)
        })

        reservaRecyclerView.adapter = adapterReserva
    }

    private fun getReservas() {
        database = FirebaseDatabase.getInstance().getReference("Reservas")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    reservasList.clear()
                    for (reservasSnapshot in snapshot.children) {
                        val reserva = reservasSnapshot.getValue(Reserva::class.java)
                        reserva?.let { reservasList.add(it) }
                    }
                    adapterReserva.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de errores
            }
        })
    }

    private fun editarReserva(reserva: Reserva) {
        // Redirigir a una nueva actividad para editar la reserva (puedes crear una actividad de edición)
        val intent = Intent(this, EditarReservaActivity::class.java)
        intent.putExtra("reserva", reserva)  // Ahora funciona porque Reserva es Serializable
        startActivity(intent)
    }

    private fun eliminarReserva(reserva: Reserva) {
        // Eliminar la reserva de Firebase
        val reservaId = reserva.id
        if (reservaId != null) {
            database.child(reservaId).removeValue().addOnSuccessListener {
                // Eliminar también de la lista y actualizar el RecyclerView
                adapterReserva.eliminarReserva(reserva)
                Snackbar.make(binding.root, "Reserva Eliminada", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
