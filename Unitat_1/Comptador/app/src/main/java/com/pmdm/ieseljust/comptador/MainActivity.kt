package com.pmdm.ieseljust.comptador

import android.content.Intent
import android.os.Bundle
import android.util.Log
//import android.widget.Button
//import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.pmdm.ieseljust.comptador.databinding.ActivityMainBinding

//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var comptador=0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Inicialitzem el TextView amb el comptador a 0
        binding.textViewComptador.text = comptador.toString()

        // Configurar los listeners de los botones
        binding.btAdd.setOnClickListener {
            comptador += 1
            binding.textViewComptador.text = comptador.toString()
        }

        binding.btSubtract.setOnClickListener {
            comptador -= 1
            binding.textViewComptador.text = comptador.toString()
        }

        binding.btReset.setOnClickListener {
            comptador = 0
            binding.textViewComptador.text = comptador.toString()
        }

        binding.btOpen.setOnClickListener {
            Intent(baseContext, MostraComptadorActivity::class.java).apply {
                putExtra("comptador", comptador)
                startActivity(this)
            }
        }

        /*
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencia al TextView
        val textViewContador=findViewById<TextView>(R.id.textViewComptador)
        // Referencia al boto d'Open
        val btOpen=findViewById<Button>(R.id.btOpen)

        // Inicialitzem el TextView amb el comptador a 0
        textViewContador.text=comptador.toString() // Estem fent una assignacio directament o accedinta algun metode?

        // Referencia al botón
        val btAdd=findViewById<Button>(R.id.btAdd)

        // Asociaciamos una expresióin lambda como
        // respuesta (callback) al evento Clic sobre
        // el botón
        btAdd.setOnClickListener {
            comptador++
            textViewContador.text=comptador.toString()
        }

        val btSubs = findViewById<Button>(R.id.btSubtract)
        btSubs.setOnClickListener{
            comptador--;
            textViewContador.text=comptador.toString()
        }

        val btRes = findViewById<Button>(R.id.btReset)
        btRes.setOnClickListener {
            comptador = 0
            textViewContador.text = comptador.toString()
        }

        /*btOpen.setOnClickListener{
            val intent = Intent(baseContext, MostraComptadorActivity::class.java)
            intent.putExtra("comptador", comptador)
            startActivity(intent)
        }*/

        btOpen.setOnClickListener {
            Intent(baseContext, MostraComptadorActivity::class.java).apply {
                putExtra("comptador", comptador)
                startActivity(this)
            }
        }

         */
    }

    override fun onStart() {
        super.onStart()
        Log.i("AVIS", "Entrem per Start")
    }

    override fun onRestoreInstanceState(estat: Bundle) {
        super.onRestoreInstanceState(estat)
        // Codi per a guardar l'estat
        comptador = estat.getInt("COMPTADOR")
        // Referencia al TextView
        //findViewById<TextView>(R.id.textViewComptador).text = comptador.toString()
        binding.textViewComptador.text = comptador.toString()
    }

    override fun onResume() {
        super.onResume()
        Log.i("AVIS", "Entrem per Resume")
    }
    override fun onPause() {
        super.onPause()
        Log.i("AVIS", "Entrem per Pause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("AVIS", "Entrem per Restart")
    }

    override fun onStop() {
        super.onStop()
        Log.i("AVIS", "Entrem per Stop")
    }

    override fun onSaveInstanceState(estat: Bundle) {
        super.onSaveInstanceState(estat)
        // Codi per a guardar l'estat
        estat.putInt("COMPTADOR", comptador)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("AVIS", "Entrem per Destroy")
    }

}