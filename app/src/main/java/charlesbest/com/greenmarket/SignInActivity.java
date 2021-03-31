package charlesbest.com.greenmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    AutoCompleteTextView mEmailView;
    EditText mPasswordView;
    View mProgressView;
    View mLoginFormView;
    TextView mForgetPassword;
    private FirebaseAuth auth;
    DatabaseReference mDatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmailView =  findViewById(R.id.lEmail);
        mProgressView = findViewById(R.id.login_progress);
        mPasswordView =  findViewById(R.id.lPassword);
        mForgetPassword = findViewById(R.id.forget);
        auth = FirebaseAuth.getInstance();


        Button SignIn =  findViewById(R.id.btnSignIn);
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = mEmailView.getText().toString();
                String b =mPasswordView.getText().toString();
                mProgressView.setVisibility(View.VISIBLE);
                final String email = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgressView.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                mProgressView.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        mPasswordView.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(SignInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    if (auth.getCurrentUser() != null) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                        mDatabase = FirebaseDatabase.getInstance().getReference("usersTable");
                                        mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                UserModel user = dataSnapshot.getValue(UserModel.class);
                                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("Name", user.getName());
                                                editor.putString("email", user.getEmail());
                                                editor.putString("phone", user.getPhone());
                                                editor.putString("acc", user.getAccount_type());
                                                editor.apply();
                                                Intent intent = new Intent(SignInActivity.this, DashBoardActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                mProgressView.setVisibility(View.GONE);

                                                Toast.makeText(SignInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                }
                            }
                        });
            }
        });
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent( SignInActivity.this,ResetPasswordActivity.class);
               // startActivity(i);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }
}
