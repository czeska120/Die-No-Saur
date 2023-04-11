package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.databinding.ActivityFacebookBinding
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.fragments.FragmentBottomBtns
import java.util.*


class FacebookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacebookBinding
    private lateinit var callbackManager: CallbackManager

    private lateinit var name: TextOutline
    private lateinit var loggedinLabel: TextOutline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacebookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginBtn = binding.facebookLoginButton
        val frame = binding.settingsFramelayout2.id
        name = binding.facebookUserName
        loggedinLabel = binding.loggedinLabel

        // check if logged in
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        // assuming not logged in
        if(!isLoggedIn){
            name.visibility = View.GONE
            loggedinLabel.visibility = View.GONE
        }

        loadFragment(frame, FragmentBottomBtns())

        callbackManager = CallbackManager.Factory.create();

        loginBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                Toast.makeText(applicationContext, "Facebook Login Successful!", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onCancel() {
                // App code
                Toast.makeText(applicationContext, "Facebook Login Cancelled!", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(exception: FacebookException) {
                // App code
                Toast.makeText(applicationContext, "Facebook Login Error!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        var graphReq = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), object : GraphRequest.GraphJSONObjectCallback{
            override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
                var nameString = obj?.getString("name")
                name.text = nameString

                if(AccessToken.getCurrentAccessToken() != null){
                    name.visibility = View.VISIBLE
                    loggedinLabel.visibility = View.VISIBLE
                }
            }
        })

        val bundle = Bundle()
        bundle.putString("fields", "name, id, first_name, last_name")

        graphReq.parameters = bundle
        graphReq.executeAsync()
    }

    private var accessTokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(
            oldAccessToken: AccessToken?,
            currentAccessToken: AccessToken?
        ) {
            if (currentAccessToken == null){
                LoginManager.getInstance().logOut()

                name.visibility = View.GONE
                loggedinLabel.visibility = View.GONE
            }
        }
    }

    private fun loadFragment(frame:Int, fragment: Fragment) {
        // create a FragmentManager
        val fm = supportFragmentManager

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction = fm.beginTransaction()

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(frame, fragment)
        fragmentTransaction.commit() // save the changes
    }

    override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker.stopTracking()
    }
}