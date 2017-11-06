package instatag.com.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by RahulReign on 04-11-2017.
 */

public class ViewDataBase extends AppCompatActivity {
    private static final String TAG="ViewDataBase";
    //add firebase database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private  String userID;
    private ListView mlistView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_database_layout);
        ///declate the database object ref, this is what we use to access the DB
        //Note:unless u r sign in , this will not b usable

        mAuth= FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference();
        FirebaseUser user=mAuth.getCurrentUser();

        userID=user.getUid();

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

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this mehtod is called once with the initial value again
                //whenever data at this location is updated
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        //data snapshot snapshot the updataed db so we can iterate all data
        //so we create for loop for iterate the snapshot
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            //create the new java class as pojo  with the 3 var name email and ph no. this class help us to  read the data snapshot
            UserInformation userInformation=new UserInformation();
            userInformation.setName(ds.child(userID).getValue(UserInformation.class).getName());//set Name
            userInformation.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail());//set Email
            userInformation.setPnone_no(ds.child(userID).getValue(UserInformation.class).getPnone_no());//set Phone no

                //now create the arraylist for link all the item and than pull into list view
            Log.d(TAG,"show data:name:"+userInformation.getName());
            Log.d(TAG,"show data:email:"+userInformation.getEmail());
            Log.d(TAG,"show data:ph no:"+userInformation.getPnone_no());

            ArrayList<String> arrayList=new ArrayList<>();
            //now add the name email ph to the arraylist


            arrayList.add(userInformation.getName());
            arrayList.add(userInformation.getEmail());
            arrayList.add(userInformation.getPnone_no());

            //now creat the array adapter and attached to the list view

            ArrayAdapter mArrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
            mlistView.setAdapter(mArrayAdapter);


         }

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
