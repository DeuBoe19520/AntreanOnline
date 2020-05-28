package id.deuboe.kantahyogya.antreanonline;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import id.deuboe.kantahyogya.antreanonline.main.MainActivity;
import java.util.Arrays;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity implements OnClickListener {

  private Animation topAnimation, bottomAnimation;
  private AppCompatTextView textAntrean, textKantor;
  private AppCompatImageView imageLogo;
  private static final int SPLASH_SCREEN = 2500;
  private static final String TAG = "SignInActivity";
  private GoogleSignInClient mGoogleSignInClient;
  private FirebaseAuth mAuth;
  private SignInButton buttonSignIn;
  private ContentLoadingProgressBar progressCircular;
  private static final int RC_SIGN_IN = 161;
  private FirebaseUser mUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);

    init();

    splashHandler();

    setSignIn();
  }

  private void init() {
    topAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_animation);
    bottomAnimation = AnimationUtils
        .loadAnimation(getApplicationContext(), R.anim.bottom_animation);

    textAntrean = findViewById(R.id.text_antrean);
    textKantor = findViewById(R.id.text_kantor);
    imageLogo = findViewById(R.id.image_logo);
    buttonSignIn = findViewById(R.id.button_sign_in);
    setButtonSignIn(buttonSignIn);
    progressCircular = findViewById(R.id.progress_circular);

    mAuth = FirebaseAuth.getInstance();
    mUser = mAuth.getCurrentUser();
  }

  private void setButtonSignIn(SignInButton signInButton) {
    signInButton.setSize(SignInButton.SIZE_WIDE);
    signInButton.setOnClickListener(this);
  }

  private void splashHandler() {
    textAntrean.setAnimation(topAnimation);
    textKantor.setAnimation(topAnimation);
    imageLogo.setAnimation(bottomAnimation);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        buttonSignIn.setVisibility(View.VISIBLE);
      }
    }, SPLASH_SCREEN);
  }

  private void setSignIn() {
    mAuth = FirebaseAuth.getInstance();

    GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

    mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
  }

  private void setIntent() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      handleTask(task);
    }
  }

  private void handleTask(Task<GoogleSignInAccount> task) {
    try {
      GoogleSignInAccount account = task.getResult(ApiException.class);
      if (account != null) {
        authWithGoogle(account);
      }
    } catch (ApiException e) {
      Log.w(TAG, "signInFailure: failureCode = " + e.getStatusCode());
      Toast.makeText(this, "SignIn Failed\n\t" + e.getMessage(), Toast.LENGTH_SHORT)
          .show();
    }
  }

  private void authWithGoogle(GoogleSignInAccount account) {
    Log.d(TAG, "authWithGoogle: " + account.getId());

    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    mAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              Log.d(TAG, "SignIn Success");
              startActivity(new Intent(getApplicationContext(), MainActivity.class));
              finish();
              Toast.makeText(SignInActivity.this, getString(R.string.selamat_datang)
                  + "\n\t" + Objects.requireNonNull(mUser).getDisplayName(), Toast.LENGTH_SHORT)
                  .show();
            }
          }
        })
        .addOnFailureListener(this, e -> {
          Log.w(TAG, "SignIn Failure" + Arrays.toString(e.getStackTrace()));
          Toast.makeText(SignInActivity.this, "Gagal Login\n\t" + e.getMessage(),
              Toast.LENGTH_SHORT).show();
        });
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.button_sign_in) {
      setIntent();
      buttonSignIn.setVisibility(View.GONE);
      progressCircular.setVisibility(View.VISIBLE);
    }
  }
}
