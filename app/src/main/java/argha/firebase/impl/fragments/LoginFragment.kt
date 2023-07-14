package argha.firebase.impl.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import argha.firebase.impl.R
import com.google.firebase.auth.FirebaseAuth

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var context: Context? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var loginBtn: Button
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    private var loginListener: LoginListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        loginBtn.setOnClickListener(View.OnClickListener {
            var email = emailField.text.toString()
            var password = passwordField.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                loginListener?.onLoginError("Both Email and Password is required")
            } else {
                loginWithEmailAndPassword(email, password);
            }
        })
    }

    private fun loginWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    loginListener?.onLoginSuccess()
                } else {
                    loginListener?.onLoginError(task.exception.toString())
                }
            }
    }

    fun init(view: View) {
        auth = FirebaseAuth.getInstance()
        loginBtn = view.findViewById(R.id.loginBtn)
        emailField = view.findViewById(R.id.emailField)
        passwordField = view.findViewById(R.id.passwordField)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, loginListener: LoginListener) =
            LoginFragment().apply {
                this.loginListener = loginListener
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

interface LoginListener {
    fun onLoginSuccess()
    fun onLoginError(message: String)
}