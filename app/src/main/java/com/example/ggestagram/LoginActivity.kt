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
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null
    var googleSignInClient : GoogleSignInClient? = null
    private val GOOGLE_LOGIN_CODE = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()



        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("317986852594-fjheqr2faod4ppvdlcuis7kamu0gsqqp.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        //이메일 로그인
        login_btn1.setOnClickListener {
            signinAndSignup()
        }
        //구글 로그인
        login_btn3.setOnClickListener {
           googleLogin()
        }

    }
    fun googleLogin(){
        var signinIntent = googleSignInClient!!.signInIntent
        loginLauncher.launch(signinIntent)
    }
    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        Log.e("REQSULT CODE Start= ",it.resultCode.toString())
        if(it.resultCode == Activity.RESULT_OK){
            Log.e("REQSULT CODE IN= ",it.resultCode.toString())
            Log.e("REQUEST CODE In = ",it.resultCode.toString())
            val task = Auth.GoogleSignInApi.getSignInResultFromIntent(it.data!!)
            if(task!!.isSuccess){
                var account = task.signInAccount
                firebaseAuthWithGoogle(account)
            }
            /*var task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try{
                var account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
                Log.d("GoogleLogin","Firebase Auth "+account.id)

            }catch(e:ApiException){
                Log.d("GoogleLogin", "Google Login Failed")
            }
*/
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)?.
        addOnCompleteListener {
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
        auth?.createUserWithEmailAndPassword(login_email_edittext.text.toString(),login_pw_edittext.text.toString())?.
        addOnCompleteListener {
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