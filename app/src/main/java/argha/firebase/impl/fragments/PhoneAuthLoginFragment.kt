package argha.firebase.impl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import argha.firebase.impl.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PhoneAuthLoginFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth : FirebaseAuth
    private lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var verficationCode: String

    private lateinit var phoneField: EditText
    private lateinit var otpField: EditText
    private lateinit var sendOtpBtn: Button
    private lateinit var verifyOtpBtn: Button

    private var phoneAuthListener: PhoneAuthListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        sendOtpBtn.setOnClickListener(View.OnClickListener {
            if(phoneField.text.toString().trim().isEmpty()) {
                phoneAuthListener?.onPhoneAuthFailed("Invalid Phone Number")
            } else if(phoneField.text.toString().trim().length != 10) {
                phoneAuthListener?.onPhoneAuthFailed("Type valid Phone number")
            } else {
                sendOtp(phoneField.text.toString().trim())
            }
        })

        verifyOtpBtn.setOnClickListener(View.OnClickListener {
            verifyOtp(verficationCode, otpField.text.trim().toString())
        })
    }

    fun sendOtp(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phoneNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(verificationId : String, code : String) {
        var credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code)
        loginWithPhone(credential)
    }

    fun loginWithPhone(phoneAuthCredential: PhoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener(requireActivity()) {
                if(it.isSuccessful) {
                    phoneAuthListener?.onPhoneAuthSuccess()
                } else {
                    phoneAuthListener?.onPhoneAuthFailed(it.exception.toString())
                }
            }
    }

    fun init(v: View) {
        auth = Firebase.auth
        phoneField = v.findViewById(R.id.phoneField)
        otpField = v.findViewById(R.id.otpField)
        sendOtpBtn = v.findViewById(R.id.sendOtpBtn)
        verifyOtpBtn = v.findViewById(R.id.verifyOtpBtn)
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                loginWithPhone(credential)
            }
            override fun onVerificationFailed(exception: FirebaseException) {
                phoneAuthListener?.onPhoneAuthFailed(exception.toString())
            }
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                verficationCode = verificationId
                phoneAuthListener?.onPhoneAuthFailed("An OTP has been send")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_phone_auth_login, container, false)
    }

    companion object {
        fun newInstance(param1: String, param2: String, phoneAuthListener: PhoneAuthListener) =
            PhoneAuthLoginFragment().apply {
                this.phoneAuthListener = phoneAuthListener
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

interface PhoneAuthListener {
    fun onPhoneAuthSuccess();
    fun onPhoneAuthFailed(message: String)
}