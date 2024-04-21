package mx.tecm.ciudadhidalgo.practica

import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class MainActivity3 : AppCompatActivity() {
    lateinit var tvNewId: TextView
    lateinit var etNewName: EditText
    lateinit var etNewType: EditText
    lateinit var etNewValue: EditText
    lateinit var btnNewCancel: Button
    lateinit var btnNewSave: Button

    private lateinit var sesion: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        tvNewId = findViewById(R.id.tvNewId)
        etNewName = findViewById(R.id.etNewName)
        etNewType = findViewById(R.id.etNewType)
        etNewValue = findViewById(R.id.etNewValue)
        btnNewCancel = findViewById(R.id.btnNewCancel)
        btnNewSave = findViewById(R.id.btnNewSave)

        sesion = getSharedPreferences("sesion", 0)

        btnNewCancel.setOnClickListener { finish() }

        if(intent.extras != null){
            tvNewId.text=intent.extras!!.getString("id")
            etNewName.setText(intent.extras!!.getString("name"))
            etNewType.setText(intent.extras!!.getString("type"))
            etNewValue.setText(intent.extras!!.getString("value"))
            btnNewSave.setOnClickListener { saveChanges() }
        }else{
            btnNewSave.setOnClickListener{ saveNew() }
        }
    }

    private fun saveChanges() {
        //se obtienen los valores de los campos de texto
        val name = etNewName.text.toString()
        val type = etNewType.text.toString()
        val value = etNewValue.text.toString()
        //si algun campo esta vacio no se hace nada
        if (name.isEmpty() || type.isEmpty() || value.isEmpty()) {
            return
        }
        //se crea un objeto JSON con los valores de los campos de texto que se deben enviar al API REST
        val body = JSONObject()
        body.put("name", name)
        body.put("type", type)
        body.put("value", value)

        //se crea la URL para hacer la peticion PUT al API REST agregando el id del sensor a actualizar tvNewId
        val url = Uri.parse(Config.URL + "sensors/" + tvNewId.text.toString())
            .buildUpon()
            .build().toString()
        //se crea la peticion con JsonObjectRequest para realizar el PUT con los datos de JSONObject body a enviar
        //el API REST regresa un JSON con los datos del senson actualizado, por lo que se puede usar JsonObjectRequest o StringRequest
        //sin embargo la forma de enviar los datos cambia en el StringRequest, ver el ejemplo del login
        val peticion = object: JsonObjectRequest(Request.Method.PUT, url, body, {
                response ->
            Toast.makeText(this, "Guardado:"+response.toString(), Toast.LENGTH_LONG).show()
            finish()
        }, {
                error -> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        }){
            override fun getHeaders(): Map<String, String>{
                val body: MutableMap<String, String> = HashMap()
                body["Authorization"] = sesion.getString("jwt", "").toString()
                return body
            }
        }
        MySingleton.getInstance(applicationContext).addToRequestQueue(peticion)
    }

    private fun saveNew() {
        val url = Uri.parse(Config.URL+"sensors")
            .buildUpon()
            .build().toString()

        val body = JSONObject()
        body.put("name", etNewName.text.toString())
        body.put("type", etNewType.text.toString())
        body.put("value", etNewValue.text.toString())

        val peticion = object: JsonObjectRequest(Request.Method.POST, url, body,{ response ->
            Toast.makeText(this,"Guardado", Toast.LENGTH_LONG).show()
            finish()
        }, { error ->
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        }){
            override fun getHeaders(): Map<String, String> {
                val body: MutableMap<String, String> = HashMap()
                body["Authorization"] = sesion.getString("jwt", "").toString()
                return body
            }
        }

        MySingleton.getInstance(applicationContext).addToRequestQueue(peticion)
    }

}








