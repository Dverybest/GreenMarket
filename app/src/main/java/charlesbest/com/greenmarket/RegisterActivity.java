package charlesbest.com.greenmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    AutoCompleteTextView Name,mEmail,phoneNumber;
    EditText mPassword,mConfirmPassword;
    View mProgressView;
    View mLoginFormView;
    Button signUp;
    private FirebaseAuth auth;
    DatabaseReference mDatabase ;
    RadioGroup acc;
    String accType = "seller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Name = findViewById(R.id.regName);
        mEmail = findViewById(R.id.regEmail);
        phoneNumber = findViewById(R.id.regNumber);
        mPassword = findViewById(R.id.regPassword);
        mConfirmPassword = findViewById(R.id.regCPassword);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        signUp = findViewById(R.id.btnSignUp);

        auth = FirebaseAuth.getInstance();
        acc = findViewById(R.id.acc);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(Name.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mPassword.getText().toString().equals(mConfirmPassword.getText().toString()) ){

                    mProgressView.setVisibility(View.VISIBLE);

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_LONG).show();

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        mProgressView.setVisibility(View.GONE);
                                        Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        if (auth.getCurrentUser() != null) {

                                            FirebaseUser users = auth.getCurrentUser();

                                            mDatabase = FirebaseDatabase.getInstance().getReference("usersTable");

                                            UserModel user = new UserModel(Name.getText().toString(), mEmail.getText().toString(), phoneNumber.getText().toString(),accType);
                                            mDatabase.child(users.getUid()).setValue(user);
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("Name", user.getName());
                                            editor.putString("email", user.getEmail());
                                            editor.putString("phone", user.getPhone());
                                            editor.putString("acc", user.getAccount_type());
                                            editor.apply();
                                            Intent intent =new Intent(RegisterActivity.this, DashBoardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                }
                            });



                }else {
                    Toast.makeText(RegisterActivity.this,"Password does not match",Toast.LENGTH_LONG).show();
                }
            }
        });

        acc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.buy){
                    accType = "buyer";
                }else {
                    accType = "seller";
                }
            }
        });

    }
}
