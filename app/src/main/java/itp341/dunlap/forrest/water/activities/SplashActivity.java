package itp341.dunlap.forrest.water.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import itp341.dunlap.forrest.water.BuildConfig;
import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.models.User;
import itp341.dunlap.forrest.water.singletons.UserManager;

/*
Starting point of the launcher, and any time you open the app.
Also contains all of the firebase-auth-ui
 */

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 123;

    private RelativeLayout splashFrame;
    private Button signOnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashFrame = (RelativeLayout) findViewById(R.id.splash_relative);
        signOnButton = (Button) findViewById(R.id.splash_sign_in_button);
        signOnButton.setOnClickListener(this);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            signOnButton.setVisibility(View.INVISIBLE);
            UserManager.getInstance().setUser(new User(auth.getCurrentUser()));
            startActivity(new Intent(this, MainActivity.class));
        }

        //Set the map boolean to "I haven't visited before"
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        if(!sp.contains(getString(R.string.map_first_bool)))
            sp.edit().putBoolean(getString(R.string.map_first_bool), false);
    }

    //Used to recieve Auth info from the Firebase-UI
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                UserManager.getInstance()
                        .setUser(new User(FirebaseAuth.getInstance().getCurrentUser()));
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Snackbar.make(splashFrame, R.string.sign_in_cancelled, Snackbar.LENGTH_SHORT);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(splashFrame, R.string.no_internet, Snackbar.LENGTH_SHORT);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Snackbar.make(splashFrame, R.string.unknown_error, Snackbar.LENGTH_SHORT);
                    return;
                }
            }

            Snackbar.make(splashFrame, R.string.unknown_sign_in_response, Snackbar.LENGTH_SHORT);
        }
    }

    //Exclusively for launching the auth if the user presses sign on
    @Override
    public void onClick(View v) {
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder().setProviders(
                        Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                        .setTheme(R.style.FirebaseTheme)
                        .build(),
                RC_SIGN_IN);
    }
}
