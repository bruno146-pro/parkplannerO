package com.example.eva3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class EditarPerfil : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editTextOldPassword: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var buttonGuardar: Button
    private lateinit var buttonBorrarCuenta: Button
    private lateinit var buttonVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editarperfil)

        auth = FirebaseAuth.getInstance()

        editTextOldPassword = findViewById(R.id.editTextOldPassword) // EditText para la contraseña actual
        editTextNewPassword = findViewById(R.id.editTextNewPassword) // EditText para la nueva contraseña
        buttonGuardar = findViewById(R.id.buttonGuardar)
        buttonBorrarCuenta = findViewById(R.id.buttonBorrarCuenta)
        buttonVolver = findViewById(R.id.buttonVolver)

        // Guardar cambios (solo contraseña)
        buttonGuardar.setOnClickListener {
            val oldPassword = editTextOldPassword.text.toString().trim()
            val newPassword = editTextNewPassword.text.toString().trim()

            if (oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                val user = auth.currentUser

                // Asegurarse de que el usuario está autenticado
                if (user != null) {
                    val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword) // Credential con la contraseña actual

                    // Reautenticación para poder cambiar la contraseña
                    user.reauthenticate(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Cambiar la contraseña
                                user.updatePassword(newPassword)
                                    .addOnCompleteListener { passwordTask ->
                                        if (passwordTask.isSuccessful) {
                                            Toast.makeText(this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show()
                                        } else {
                                            passwordTask.exception?.let { exception ->
                                                Toast.makeText(this, "Error al cambiar la contraseña: ${exception.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                            } else {
                                Toast.makeText(this, "Error de reautenticación: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } else {
                Toast.makeText(this, "Por favor ingrese la contraseña actual y la nueva contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // Borrar cuenta
        buttonBorrarCuenta.setOnClickListener {
            val user = auth.currentUser
            user?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Cuenta borrada exitosamente", Toast.LENGTH_SHORT).show()
                    // Redirigir al login o pantalla principal
                    startActivity(Intent(this, IniciarSesion::class.java))
                    finish()
                } else {
                    task.exception?.let { exception ->
                        Toast.makeText(this, "Error al borrar la cuenta: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Volver al menú
        buttonVolver.setOnClickListener {
            startActivity(Intent(this, Menu::class.java))
            finish()
        }
    }
}
