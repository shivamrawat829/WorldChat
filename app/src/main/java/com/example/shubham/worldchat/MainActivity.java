package com.example.shubham.worldchat;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button chat;
    private FirebaseAuth mAuth;
    public  String gemder,country;
    Boolean online,iamconnected;
    ProgressDialog progressDialog;
    DataSnapshot childSnapshot;
    String otheruserid,Whomiconnectedto;
    private DatabaseReference mDatabase;
    FirebaseUser mCurrentUserId;
    String curentuserid;

    private String TAG="MainActivity";
    TextView currentuser;
    @Override
    protected void onPostResume() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mDatabase.child("Online").setValue(false);
        super.onResume();
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mDatabase.child("Online").setValue(false);
        super.onResume();


        imNotConnected();

    }

    private void imNotConnected() {
        curentuserid= mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(curentuserid).child("isconnected");
        mDatabase.setValue(false);
    }



    @Override
    protected void onStart() {
        FirebaseUser currentuser =mAuth.getCurrentUser();

        if (currentuser == null)
        {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
            finish();
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        currentuser = (TextView) findViewById(R.id.currentuser);

FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();


        mCurrentUserId = mAuth.getCurrentUser();
        // currentuser.setText(mCurrentUserId);

        if (mCurrentUserId==null){
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
            finish();
        }


        chat = (Button) findViewById(R.id.chat);




        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkifiamconnected();


                progressDialog.setMessage("Wait");
                progressDialog.show();

                isonline();



            }
        });
    }

    private void checkifiamconnected() {
        curentuserid= mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(curentuserid).child("isconnected");


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                iamconnected  = (Boolean) dataSnapshot.getValue();
                Toast.makeText(getApplicationContext(),"I am connected value"+iamconnected,Toast.LENGTH_LONG).show();

                if (iamconnected==true)

                {

                    checkwhomiconnedtedto();

                }
                else {

                    Toast.makeText(getApplicationContext(),"value is"+iamconnected,Toast.LENGTH_LONG).show();
                    randomiddta();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void checkwhomiconnedtedto() {

        curentuserid= mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(curentuserid).child("connectedto");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Whomiconnectedto  = (String) dataSnapshot.getValue();


                Intent intent = new Intent(MainActivity.this,ChattingSection.class);
                Toast.makeText(getApplicationContext(),"WHom is connected to working",Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putString("randomid", Whomiconnectedto);
                intent.putExtras(bundle);

                startActivity(intent);
                finish();




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void randomiddta() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int questionCount = (int) dataSnapshot.getChildrenCount();

                Log.d(TAG, "onDataChange: "+questionCount);
                //     Toast.makeText(getApplicationContext(),"value is "+questionCount,Toast.LENGTH_LONG).show();

                Iterator itr = dataSnapshot.getChildren().iterator();

                Random no = new Random();
                int rndNum = no.nextInt(questionCount-1) + 1;

                for(int i = 0; i < rndNum; i++) {
                    itr.next();
                }
                childSnapshot = (DataSnapshot) itr.next();

                otheruserid = childSnapshot.getKey();
                Toast.makeText(getApplicationContext(),"other user id is "+otheruserid,Toast.LENGTH_LONG).show();

                checkcountry();
                checkgender();
                checkavailablity();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkavailablity() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(otheruserid).child("Country");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                country = (String) dataSnapshot.getValue();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    private void checkgender() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(otheruserid).child("Gender");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gemder = (String) dataSnapshot.getValue();


                Toast.makeText(getApplicationContext(), "online is"+online, Toast.LENGTH_LONG).show();
                if (Objects.equals(gemder, "Male")&&Objects.equals(country, "India")
                        &&online==true&& !Objects.equals(otheruserid, mAuth.getCurrentUser().getUid())) {

                    Intent intent = new Intent(MainActivity.this,ChattingSection.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("randomid", otheruserid);
                    intent.putExtra("EXIT", true);
                    intent.putExtras(bundle);




                    otherisConnected();
                    iamconnected();
                    iamconnectedto();
                    otherisconnectedtome();





                    startActivity(intent);
                    finish();

                    progressDialog.dismiss();

                } else
                {

                    checkifiamconnected();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void otherisconnectedtome() {
        curentuserid= mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(otheruserid).child("connectedto");

        mDatabase.setValue(curentuserid);
    }


    private void iamconnected() {
        curentuserid= mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(curentuserid).child("isconnected");
        mDatabase.setValue(true);
    }


    private void iamconnectedto() {
        curentuserid= mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(curentuserid).child("connectedto");

        mDatabase.setValue(otheruserid);


    }


    private void otherisConnected() {


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(otheruserid).child("isconnected");
        mDatabase.setValue(true);


    }

    private void checkcountry() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(otheruserid).child("Online");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                online  = (Boolean) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void isonline() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mDatabase.child("Online").setValue(true);

    }

}