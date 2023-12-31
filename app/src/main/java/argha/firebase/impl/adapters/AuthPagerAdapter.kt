package argha.firebase.impl.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import argha.firebase.impl.fragments.LoginFragment
import argha.firebase.impl.fragments.LoginListener
import argha.firebase.impl.fragments.PhoneAuthListener
import argha.firebase.impl.fragments.PhoneAuthLoginFragment
import argha.firebase.impl.fragments.SignUpListener
import argha.firebase.impl.fragments.SignupFragment
import java.lang.IllegalArgumentException

class AuthPagerAdapter(
    fm: FragmentManager,
    private val loginListener: LoginListener,
    private val signUpListener: SignUpListener,
    private val phoneAuthListener: PhoneAuthListener
) : FragmentStatePagerAdapter(fm) {

    private val tabs = arrayOf("Login", "SignUp", "Phone")

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs.get(position)
    }

    override fun getCount(): Int {
        return tabs.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment.newInstance("", "", loginListener)
            1 -> SignupFragment.newInstance("", "", signUpListener)
            2 -> PhoneAuthLoginFragment.newInstance("", "", phoneAuthListener)
            else -> throw IllegalArgumentException("Invalid Tab Position")
        }
    }

}