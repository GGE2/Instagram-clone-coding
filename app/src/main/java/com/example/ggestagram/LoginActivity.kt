package com.example.ggestagram

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInApi
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null
    var googleSignInClient : GoogleSignInClient? = null
    private val GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        login_btn1.setOnClickListener {
            signinAndSignup()
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken("317986852594-fjheqr2faod4ppvdlcuis7kamu0gsqqp.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
    }
    fun googleLogin(){

        val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                if(task.isSuccessful){

                }

            }
        }


    }



    fun signinAndSignup(){
        auth?.createUserWithEmailAndPassword(login_email_edittext.text.toString(),login_pw_edittext.text.toString())?.addOnCompleteListener {
             it->
            if(it.isSuccessful){
                Log.e(TAG,"signup ")
                moveMainPage(it.result.user)

                //Create a user account
            }else if(it.exception?.message.isNullOrEmpty()){
                //Show the error message
                Log.e(TAG,"signup null ")
                Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
            }else{
                Log.e(TAG,"signinEmail ")
                signinEmail()
                //Login if you have account
            }


        }

    }
    fun signinEmail(){
        auth?.createUserWithEmailAndPassword(login_email_edittext.text.toString(),login_pw_edittext.text.toString())?.addOnCompleteListener {
                it->
            if(it.isSuccessful){
                //Login Success
                Log.e(TAG,"signinEmail ")
                moveMainPage(it.result.user)
            }
            else {
                //Show the error message
                Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()

            }

        }

    }
    fun moveMainPage(user: FirebaseUser?){
        if(user!=null){
            Log.e(TAG,"movemainpage ")
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

}