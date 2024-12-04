package com.example.eva3.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eva3.Models.Reserva
import com.example.eva3.R
import com.google.android.material.button.MaterialButton

class AdapterReserva(private var reservas: ArrayList<Reserva>, private val onEditClick: (Reserva) -> Unit, private val onDeleteClick: (Reserva) -> Unit) :
    RecyclerView.Adapter<AdapterReserva.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        val patente: TextView = itemView.findViewById(R.id.tvPatente)
        val btnEditar: MaterialButton = itemView.findViewById(R.id.btnEditaritem)
        val btnEliminar: MaterialButton = itemView.findViewById(R.id.btnEliminaritem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reservas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reserva = reservas[position]

        holder.nombre.text = reserva.nombre
        holder.patente.text = reserva.patente

        // Evento de clic para editar
        holder.btnEditar.setOnClickListener {
            onEditClick(reserva)
        }

        // Evento de clic para eliminar
        holder.btnEliminar.setOnClickListener {
            onDeleteClick(reserva)
        }
    }

    override fun getItemCount(): Int {
        return reservas.size
    }

    // Método para eliminar la reserva de la lista y notificar al adaptador
    fun eliminarReserva(reserva: Reserva) {
        val position = reservas.indexOf(reserva)
        if (position != -1) {
            reservas.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
