package instatag.com.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by RahulReign on 03-11-2017.
 */

public class AddtoDataBase extends AppCompatActivity {
    private static final String TAG="AddtoDataBase";
    private EditText enternewFood;
    private Button addtoDB;
    //add firebase database stuff
private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_database_layout);
        enternewFood=(EditText)findViewById(R.id.addFood);
        addtoDB=(Button)findViewById(R.id.btnaddnewfood);

        //declare the database reference objedt, This is what we use to access the DB
        //Note:unless you are sign in ,This will not be usable
        mAuth= FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference();

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
        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               // String value = dataSnapshot.getValue(String.class);
                Object value= dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        addtoDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFood=enternewFood.getText().toString();
                if(!newFood.equals("")){
                    //if newfood does not equal to empty
                    //than we can proceed
                    //now here we need to that firebase user Database key so for this
                    FirebaseUser user=mAuth.getCurrentUser();
                    String userID=user.getUid();//this one is a user database key on firebase console
                    mRef.child(userID).child("Food").child("FastFood").child(newFood).setValue(true);
                    //now show on toast msg for adding new food
                    toastMessage("Adding"+newFood+"to database");
                    //reset the text
                    enternewFood.setText("");




                }
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


