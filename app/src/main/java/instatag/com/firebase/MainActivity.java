package instatag.com.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private  static final String TAG="MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailtext,mpasswordtext;
    private Button signinbtn,signoutbtn,btnAddtoDataBase,viewItemScreen,btnforUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmailtext=(EditText)findViewById(R.id.email);
        mpasswordtext=(EditText)findViewById(R.id.password);
        signinbtn=(Button)findViewById(R.id.email_sign_in_button);
        signoutbtn=(Button)findViewById(R.id.email_sign_out_button);
        btnAddtoDataBase=(Button)findViewById(R.id.add_item_screen);
        viewItemScreen=(Button)findViewById(R.id.view_item_screen);
        btnforUpload=(Button)findViewById(R.id.uploadimageid);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Succesfully signin with:"+user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("succesfully sign out");
                }
                // ...
            }
        };
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmailtext.getText().toString();
                String pw=mpasswordtext.getText().toString();
                //if user doesnt enter any email or pw
                if(!email.equals("")&& !pw.equals("")){
                    mAuth.signInWithEmailAndPassword(email,pw);

                }
                else{
                    toastMessage("you didnt type the credentials");
                }
            }


        });
        signoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                toastMessage("signing out....");
            }
        });
        btnAddtoDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddtoDataBase.class);
                startActivity(intent);
            }
        });
        viewItemScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ViewDataBase.class);
                startActivity(intent);
            }
        });
        btnforUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Upload.class);
                startActivity(intent);

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

    }

}
