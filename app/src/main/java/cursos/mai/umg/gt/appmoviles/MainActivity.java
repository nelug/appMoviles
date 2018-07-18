package cursos.mai.umg.gt.appmoviles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "";
    SignInButton mGoogleSignInButton;
    GoogleApiClient mGoogleApiClient;

    CallbackManager callbackManager;
    LoginButton loginButton;
    TextView txtMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMainActivity = (TextView) findViewById(R.id.txtMainActivity);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn){
            Profile profile = Profile.getCurrentProfile();
            txtMainActivity.setText(profile.getName().toString());
        }

        txtMainActivity.setText(accessToken.getUserId());
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Profile profile = Profile.getCurrentProfile();
                        txtMainActivity.setText(profile.getName().toString());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        mGoogleSignInButton = (SignInButton)findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                final GoogleApiClient client = mGoogleApiClient;
                //handleSignInResult(...)
            } else {
                //handleSignInResult(...);
            }
        } else {
            // Handle other values for requestCode
        }
    }

    @Override
    public void onClick(View view) {

    }


    private void signInWithGoogle() {

        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
