package com.example.eva3.Models

import java.io.Serializable

data class Reserva(
    val id: String? = null,
    var nombre: String = "",
    var patente: String = ""
) : Serializable