package net.cmadrid.www.enelpay

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.view.*
import org.json.JSONObject
import java.lang.Exception
import android.R.attr.name
import android.content.Intent


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bValidarRed = findViewById<Button>(R.id.bValidarRed);
        val bContinuar = findViewById<Button>(R.id.bContinuar);

        ChequeaRed()


        bValidarRed.setOnClickListener{
            ChequeaRed()
        }

        bContinuar.setOnClickListener{

            val TAG = "net.cmadrid.www.enelpay.MainActivity.empresa"
            val spinner = findViewById(R.id.sPaises) as Spinner
            val text = spinner.getSelectedItem().toString()
            Log.d("Seleccion------>",text)
            val intent = Intent(this,Captura::class.java)
            intent.putExtra(TAG,text)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
        System.exit(0)
        return
    }


    private fun AgregaPaises()
    {
        val spinner = findViewById(R.id.sPaises) as Spinner

        val queue = Volley.newRequestQueue(this)
        val solicitud = StringRequest(Request.Method.GET, "http://lubricantesenlinea.cl/statusrdd/empresas.php", Response.Listener<String>{
                response ->
            try{
                val json = JSONObject(response)
                Log.d("Listado de Empresas->",response)
                val empresas = json.getJSONArray("empresas")
                val setdeEmpresas = arrayOfNulls<String>(empresas.length())

                for ( i in 0 .. empresas.length() -1 )
                {
                    val id_empresa = empresas.getJSONObject(i).getString("id_empresa")
                    val nom_empresa = empresas.getJSONObject(i).getString("nombre")
                    setdeEmpresas[i] = nom_empresa
                }
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, setdeEmpresas)

                spinner.setAdapter(adapter)
            }
            catch(e: Exception){
                Log.d("Listado de Empresas->",e.toString())
            }
        }, Response.ErrorListener {  })

        queue.add(solicitud)

    }

    private fun ChequeaRed()
    {
        val bValidarRed = findViewById<Button>(R.id.bValidarRed);
        val textTitlePaises = findViewById<TextView>(R.id.textTitlePaises)
        val sPaises = findViewById<Spinner>(R.id.sPaises)
        val bContinuar = findViewById<Button>(R.id.bContinuar);

        if (Network.hayRed(this))
        {
            AgregaPaises()
            //Toast.makeText(this, "Hay red", Toast.LENGTH_LONG).show()
            bValidarRed.setVisibility(View.GONE)
            textTitlePaises.setVisibility(View.VISIBLE)
            sPaises.setVisibility(View.VISIBLE)
            bContinuar.setVisibility(View.VISIBLE)

        }else
        {
            Toast.makeText(this, "Asegurese que tiene conexión a Internet", Toast.LENGTH_LONG).show()
            bValidarRed.setVisibility(View.VISIBLE)
        }
    }



}
