package net.cmadrid.www.enelpay

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class Pago : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        val Metodo1 = findViewById<ImageView>(R.id.Metodo1)
        val Metodo2 = findViewById<ImageView>(R.id.Metodo2)
        val Metodo3 = findViewById<ImageView>(R.id.Metodo3)
        val Metodo4 = findViewById<ImageView>(R.id.Metodo4)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar2)

        val url1 = findViewById<TextView>(R.id.url1)
        val url2 = findViewById<TextView>(R.id.url2)
        val url3 = findViewById<TextView>(R.id.url3)
        val url4 = findViewById<TextView>(R.id.url4)

        Metodo1.setVisibility(View.INVISIBLE)
        Metodo2.setVisibility(View.INVISIBLE)
        Metodo3.setVisibility(View.INVISIBLE)
        Metodo4.setVisibility(View.INVISIBLE)
        progressBar.setVisibility(View.VISIBLE)


        val suministro = intent.getStringExtra("net.cmadrid.www.enelpay.Captura.suministro")
        val nombre = intent.getStringExtra("net.cmadrid.www.enelpay.Captura.nombre")
        val monto = intent.getStringExtra("net.cmadrid.www.enelpay.Captura.monto")
        val empresa = intent.getStringExtra("net.cmadrid.www.enelpay.Captura.empresa")
        val direccion = intent.getStringExtra("net.cmadrid.www.enelpay.Captura.direccion")

        Metodo1.setOnClickListener {
            val TAG = "net.cmadrid.www.enelpay.Pago.url"
            val intent = Intent(this,PagoWeb::class.java)
            intent.putExtra(TAG,url1.text.toString())
            startActivity(intent)
        }
        Metodo2.setOnClickListener {
            val TAG = "net.cmadrid.www.enelpay.Pago.url"
            val intent = Intent(this,PagoWeb::class.java)
            intent.putExtra(TAG,url2.text.toString())
            startActivity(intent)
        }
        Metodo3.setOnClickListener {
            val TAG = "net.cmadrid.www.enelpay.Pago.url"
            val intent = Intent(this,PagoWeb::class.java)
            intent.putExtra(TAG,url3.text.toString())
            startActivity(intent)
        }
        Metodo4.setOnClickListener {
            val TAG = "net.cmadrid.www.enelpay.Pago.url"
            val intent = Intent(this,PagoWeb::class.java)
            intent.putExtra(TAG,url4.text.toString())
            startActivity(intent)
        }


        val queue = Volley.newRequestQueue(this)
        val solicitud = StringRequest(Request.Method.GET, "http://lubricantesenlinea.cl/statusrdd/pago.php?empresa="+empresa+"&suministro="+suministro+"&monto="+monto+"&nombre="+nombre+"&direccion="+direccion, Response.Listener<String>{
                response ->
            try{
                Log.d("Respuesta Lectura 2->",response)
                val json = JSONObject(response)
                val urls = json.getJSONArray("Urls")

                for ( i in 0 .. urls.length() -1 )
                {
                    val url_logo = urls.getJSONObject(i).getString("Image_url")
                    Log.d("Respuesta Lectura 2->",url_logo)

                    when (i){
                        0 ->{
                            Picasso.get().load(url_logo).into(Metodo1)
                            Metodo1.setVisibility(View.VISIBLE)
                            url1.text = urls.getJSONObject(i).getString("Btn_url")
                        }
                        1 ->{
                            Picasso.get().load(url_logo).into(Metodo2)
                            Metodo2.setVisibility(View.VISIBLE)
                            url2.text = urls.getJSONObject(i).getString("Btn_url")
                        }
                        2 ->{
                            Picasso.get().load(url_logo).into(Metodo3)
                            Metodo3.setVisibility(View.VISIBLE)
                            url3.text = urls.getJSONObject(i).getString("Btn_url")
                        }
                        3 ->{
                            Picasso.get().load(url_logo).into(Metodo4)
                            Metodo4.setVisibility(View.VISIBLE)
                            url4.text = urls.getJSONObject(i).getString("Btn_url")
                        }
                    }

                }

                progressBar.setVisibility(View.INVISIBLE)
            }
            catch(e: Exception){
                Log.d("Respuesta Lectura->",e.toString())
            }
        }, Response.ErrorListener {  })

        queue.add(solicitud)


    }


}
