package net.cmadrid.www.enelpay

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject
import java.lang.Exception

class Captura : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        var Inicio=1
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_captura)
        val empresa = intent.getStringExtra("net.cmadrid.www.enelpay.MainActivity.empresa")
        //Toast.makeText(this,empresa, Toast.LENGTH_SHORT).show();
        val bReintentar = findViewById<Button>(R.id.bReintentar)
        val bPagar = findViewById<Button>(R.id.bPagar)

        if (Inicio==1) {
            Inicio=0
            LeerCodigo(empresa)
        }
        bReintentar.setOnClickListener{
            LeerCodigo(empresa)
        }

        bPagar.setOnClickListener{
            val txtSuministroNUM = findViewById<TextView>(R.id.txtSuministroNUM)
            val txtMontoNUM = findViewById<TextView>(R.id.txtMontoNUM)
            val txtNombre = findViewById<TextView>(R.id.txtNombre)
            val txtDireccion = findViewById<TextView>(R.id.txtDireccion)

            val empresa = intent.getStringExtra("net.cmadrid.www.enelpay.MainActivity.empresa")
            val TAG1 = "net.cmadrid.www.enelpay.Captura.empresa"
            val TAG2 = "net.cmadrid.www.enelpay.Captura.monto"
            val TAG3 = "net.cmadrid.www.enelpay.Captura.suministro"
            val TAG4 = "net.cmadrid.www.enelpay.Captura.nombre"
            val TAG5 = "net.cmadrid.www.enelpay.Captura.direccion"
            val intent = Intent(this,Pago::class.java)
            intent.putExtra(TAG1,empresa)
            intent.putExtra(TAG2,txtMontoNUM.text)
            intent.putExtra(TAG3,txtSuministroNUM.text)
            intent.putExtra(TAG4,txtNombre.text)
            intent.putExtra(TAG5,txtDireccion.text)
            startActivity(intent)
        }

    }


    private fun  LeerCodigo(empresa:String)
    {
        val scanner = IntentIntegrator(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        scanner.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data)
        val empresa = intent.getStringExtra("net.cmadrid.www.enelpay.MainActivity.empresa")
        val txtError = findViewById<TextView>(R.id.txtError)
        val bReintentar = findViewById<Button>(R.id.bReintentar)
        val bPagar = findViewById<Button>(R.id.bPagar)


        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtDireccion = findViewById<TextView>(R.id.txtDireccion)
        val txtMonto = findViewById<TextView>(R.id.txtMonto)
        val txtSuministro = findViewById<TextView>(R.id.txtSuministro)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val txtSuministroNUM = findViewById<TextView>(R.id.txtSuministroNUM)
        val txtMontoNUM = findViewById<TextView>(R.id.txtMontoNUM)

        txtDireccion.text = ""
        txtNombre.text = ""
        txtMonto.text = ""
        txtSuministro.text=""

        var strNumeroSuministro:String=""
        var intEmpresa:Int=0

        var strNombre:String=""
        var strResultado:String=""
        var strDireccion:String=""
        var strMonto:String=""

        txtError.text=""
        bReintentar.setVisibility(View.INVISIBLE)
        bPagar.setVisibility(View.INVISIBLE)

        progressBar.setVisibility(View.VISIBLE)
        if (result!=null)
        {
            if(result.getContents()==null)
            {
                bReintentar.setVisibility(View.VISIBLE)
                progressBar.setVisibility(View.INVISIBLE)
            }
            else
            {
                val cod_barra = result.getContents()
                if (empresa=="Enel CHILE" && cod_barra.length!=32)
                {
                    txtError.text="Código de barra no válido para el país"
                    Toast.makeText(this,"Código de barra no válido para el país", Toast.LENGTH_LONG).show()
                    bReintentar.setVisibility(View.VISIBLE)
                    progressBar.setVisibility(View.INVISIBLE)
                }
                else if (empresa=="Enel PERÚ" && cod_barra.length!=42)
                {
                    txtError.text="Código de QR no válido para el país"
                    Toast.makeText(this,"Código de barra no válido para el país", Toast.LENGTH_LONG).show()
                    bReintentar.setVisibility(View.VISIBLE)
                    progressBar.setVisibility(View.INVISIBLE)
                }
                else
                {
                    if (empresa=="Enel CHILE"){
                        intEmpresa=1
                        strNumeroSuministro = cod_barra.substring(3,10)
                    }
                    if (empresa=="Enel PERÚ"){
                        intEmpresa=2
                        strNumeroSuministro = cod_barra.substring(0,8)
                    }
                    //Toast.makeText(this,"Codigo correcto-"+strNumeroSuministro, Toast.LENGTH_LONG).show()

                    val queue = Volley.newRequestQueue(this)
                    val solicitud = StringRequest(Request.Method.GET, "http://lubricantesenlinea.cl/statusrdd/consulta_deuda.php?cod_empresa="+intEmpresa.toString()+"&suministro="+strNumeroSuministro, Response.Listener<String>{
                            response ->
                        try{
                            //Log.d("Respuesta Lectura 2->",response)
                            val json = JSONObject(response)
                            val boletas = json.getJSONArray("boleta")
                            strResultado = boletas.getJSONObject(0).getString("resultado")

                            if (strResultado=="000") {
                                strNombre = boletas.getJSONObject(0).getString("nombre")

                                strDireccion = boletas.getJSONObject(0).getString("direccion")
                                strMonto = boletas.getJSONObject(0).getString("monto")

                                txtDireccion.text = strDireccion
                                txtNombre.text = strNombre
                                txtMonto.text = "Total a Pagar\n" + strMonto
                                txtSuministro.text = "Suministro N°" + strNumeroSuministro
                                bReintentar.setVisibility(View.VISIBLE)
                                bPagar.setVisibility(View.VISIBLE)
                                txtSuministroNUM.text = strNumeroSuministro
                                txtMontoNUM.text = boletas.getJSONObject(0).getString("monto_limpio")

                            }
                            else
                            {
                                if (strResultado=="005" || strResultado=="112" ) {
                                    txtError.text = "Cliente sin deuda Vigente"
                                    bReintentar.setVisibility(View.VISIBLE)
                                }else
                                {
                                    txtError.text = strResultado
                                    bReintentar.setVisibility(View.VISIBLE)
                                }
                            }
                            progressBar.setVisibility(View.INVISIBLE)
                            //Toast.makeText(this,"Codigo correcto-"+strNombre, Toast.LENGTH_LONG).show()
                        }
                        catch(e: Exception){
                            Log.d("Respuesta Lectura->",e.toString())
                        }
                    }, Response.ErrorListener {  })

                    queue.add(solicitud)
                }
                //Toast.makeText(this,"Scanned - >"+result.getContents() + "-" + empresa + " - "+cod_barra.length, Toast.LENGTH_LONG).show()
                Log.d("Leido->",result.getContents())
            }
        }
    }

}
