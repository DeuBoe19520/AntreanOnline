package id.deuboe.kantahyogya.antreanonline;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import id.deuboe.kantahyogya.antreanonline.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

  private Animation topAnimation, bottomAnimation;
  private AppCompatTextView textAntrean, textKantor;
  private AppCompatImageView imageLogo;
  private static final int SPLASH_SCREEN = 2500;
  private static final String TAG = "SplashActivity";
  private GoogleSignInClient mGoogleSignInClient;
  private FirebaseAuth mAuth;
  private static final int RC_SIGN_IN = 161;
  private FirebaseUser mUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    init();

    checkCurrentUser();
  }

  private void init() {
    topAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_animation);
    bottomAnimation = AnimationUtils
        .loadAnimation(getApplicationContext(), R.anim.bottom_animation);

    textAntrean = findViewById(R.id.text_antrean);
    textKantor = findViewById(R.id.text_kantor);
    imageLogo = findViewById(R.id.image_logo);

    textAntrean.setAnimation(topAnimation);
    textKantor.setAnimation(topAnimation);
    imageLogo.setAnimation(bottomAnimation);

    mAuth = FirebaseAuth.getInstance();
    mUser = mAuth.getCurrentUser();
  }

  private void splashHandler() {
    new Handler().postDelayed(() -> {
      startActivity(new Intent(getApplicationContext(), MainActivity.class));
      finish();
    }, SPLASH_SCREEN);
  }

  private void checkCurrentUser() {
    if (mUser == null) {
      startActivity(new Intent(getApplicationContext(), SignInActivity.class));
      finish();
    } else {
      splashHandler();
    }
  }
}
