package argha.firebase.impl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {

    private lateinit var logoutBtn: Button
    private lateinit var emailText: TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        init()

        emailText.text = auth.currentUser?.email

        logoutBtn.setOnClickListener(View.OnClickListener {
            logoutFromSystem()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        })
    }

    fun logoutFromSystem() {
        auth.signOut()
    }

    fun init() {
        emailText = findViewById(R.id.emailText)
        logoutBtn = findViewById(R.id.logoutBtn)
        auth = Firebase.auth
    }
}