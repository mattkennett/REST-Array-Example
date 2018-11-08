package edu.ualr.mpkennett.restfularrays

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

const val URL_ADDRESS: String = "http://quizapp.mattkennett.com/rest_array.php"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client: OkHttpClient = OkHttpClient()

        val request: Request = Request.Builder()
                .url(URL_ADDRESS)
                .build()

        doAsync {
            var response: Response? = null

            try {
                response = client.newCall(request).execute()
            } catch (e: Exception) {
                Log.d("MPK_UTIL", e.toString())
            }


            if (response != null) {
                val responseBody: String = response.body()!!.string()
                val responseCode: Int = response.code()

                Log.d("MPK_UTILITY", "Body: " + responseBody)
                Log.d("MPK_UTILITY", "Code: " + responseCode.toString())

                val gson = Gson()

                val myMessage: messageResponse =
                        gson.fromJson(responseBody, messageResponse::class.java)

                uiThread {
                    val linearLayoutMessages: LinearLayout = findViewById(R.id.linearLayoutMessages)
                    linearLayoutMessages.removeAllViews()

                    val newTextView = TextView(this@MainActivity)

                    var newTextViewString: String = ""

                    if (myMessage.messages != null) {
                        for (message in myMessage.messages) {
                            newTextViewString += message + "\n"
                        }
                    }

                    newTextView.text = newTextViewString

                    linearLayoutMessages.addView(newTextView)
                }
            }
        }

    }
}
