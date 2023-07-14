package argha.firebase.impl

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import argha.firebase.impl.adapters.AuthPagerAdapter
import argha.firebase.impl.fragments.LoginListener
import argha.firebase.impl.fragments.SignUpListener
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity(), LoginListener, SignUpListener {

    private lateinit var auth : FirebaseAuth
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        init()
        setupTabsWithViewPager()
    }

    private fun setupTabsWithViewPager() {
        viewPager.adapter = AuthPagerAdapter(supportFragmentManager, this, this)
        tabLayout.setupWithViewPager(viewPager)
    }

    fun init() {
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onLoginSuccess() {
        intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSignUpSuccess() {
        intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSignUpError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
