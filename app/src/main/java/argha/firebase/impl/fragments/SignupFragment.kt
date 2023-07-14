package argha.firebase.impl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import argha.firebase.impl.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SignupFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var emailField : EditText
    private lateinit var passwordField : EditText
    private lateinit var signUpBtn : Button
    private lateinit var auth : FirebaseAuth
    private var signUpListener: SignUpListener? = null

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
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        signUpBtn.setOnClickListener(View.OnClickListener {
            var email = emailField.text.toString()
            var password = passwordField.text.toString()
            if(email.isEmpty() || password.isEmpty()) {
                signUpListener?.onSignUpError("Both Email and Password is required")
            } else if(passwordField.length() < 8) {
                signUpListener?.onSignUpError("Password min 8 to 16 characters")
            } else {
                createAccountInSystem(email, password)
            }
        })
    }

    private fun createAccountInSystem(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
                if(it.isSuccessful) {
                    signUpListener?.onSignUpSuccess()
                } else {
                    signUpListener?.onSignUpError(it.exception.toString())
                }
            }
    }

    fun init(v : View) {
        auth = Firebase.auth
        emailField = v.findViewById(R.id.emailField)
        passwordField = v.findViewById(R.id.passwordField)
        signUpBtn = v.findViewById(R.id.signupBtn)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, signUpListener: SignUpListener) =
            SignupFragment().apply {
                this.signUpListener = signUpListener
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

interface SignUpListener {
    fun onSignUpSuccess()
    fun onSignUpError(message: String)
}