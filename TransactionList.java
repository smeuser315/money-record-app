package com.example.momomanagement.Agent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momomanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class TransactionList extends AppCompatActivity implements TransactionAdapter.TransactionListener{

    private RecyclerView recordRecyclerView;
    private SearchView mainRecordSearchView;
    private TextView recordTitleTV, recordBackTV;

    private ImageView recordSearchTV, moreIV;

    private FloatingActionButton fab;

    private TransactionAdapter transactionAdapter;

    private TransactionModel transactionModel;

    private TextView nullNotice;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        moreIV = findViewById(R.id.recordABMainMoreIcon);
        moreIV.setVisibility(View.GONE);


        recordRecyclerView = findViewById(R.id.recordMainRV);
        recordRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recordRecyclerView.setLayoutManager(new LinearLayoutManager(TransactionList.this));


        mainRecordSearchView = findViewById(R.id.recordABMainSV);
        recordTitleTV = findViewById(R.id.mainRecordABTitle);
        recordSearchTV = findViewById(R.id.recordABMainSearchTV);
        recordBackTV = findViewById(R.id.recordABMainSearchBackTV);
        fab = findViewById(R.id.recordMainFAB);

        progressBar = findViewById(R.id.pb);
        nullNotice = findViewById(R.id.nullNoticeRU);

        int id = mainRecordSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = mainRecordSearchView.findViewById(id);

        if(searchText != null){
            searchText.setHintTextColor(Color.WHITE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getStringExtra("network").equals("MTN")){
                    Intent intent = new Intent(TransactionList.this, AddTransactions.class);
                    intent.putExtra("network","MTN");
                    intent.putExtra("recordID", getIntent().getStringExtra("recordID"));
                    startActivity(intent);
                } else if (getIntent().getStringExtra("network").equals("Telecel")) {
                    Intent intent = new Intent(TransactionList.this, AddTransactions.class);
                    intent.putExtra("network","Telecel");
                    intent.putExtra("recordID", getIntent().getStringExtra("recordID"));
                    startActivity(intent);
                } else if (getIntent().getStringExtra("network").equals("Airteltigo")) {
                    Intent intent = new Intent(TransactionList.this, AddTransactions.class);
                    intent.putExtra("network","Airteltigo");
                    intent.putExtra("recordID", getIntent().getStringExtra("recordID"));
                    startActivity(intent);
                }
                else if (getIntent().getStringExtra("network").equals("Company")) {
                    Intent intent = new Intent(TransactionList.this, AddTransactions.class);
                    intent.putExtra("network","Company");
                    intent.putExtra("recordID", getIntent().getStringExtra("recordID"));
                    startActivity(intent);
                }

            }
        });

        if (getIntent().getStringExtra("network").equals("MTN")){
            recordTitleTV.setText("MTN Transactions");
        } else if (getIntent().getStringExtra("network").equals("Telecel")) {
            recordTitleTV.setText("Telecel Transactions");
        } else if (getIntent().getStringExtra("network").equals("AirtelTigo")) {
            recordTitleTV.setText("AirtelTigo Transactions");
        }
        else if (getIntent().getStringExtra("network").equals("Company")) {
            recordTitleTV.setText("Company Transactions");
        }
        else{
            recordTitleTV.setText("RECORDS");
        }

        FirebaseFirestore.getInstance()
                .collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getString("userType").equals("Admin")){
                            fab.setVisibility(View.GONE);
                        }
                        else {
                            fab.setVisibility(View.VISIBLE);
                        }
                    }
                });

        recordSearchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordSearchTV.setVisibility(View.INVISIBLE);
                recordTitleTV.setVisibility(View.INVISIBLE);
                recordBackTV.setVisibility(View.VISIBLE);
                mainRecordSearchView.setVisibility(View.VISIBLE);
                mainRecordSearchView.setIconifiedByDefault(true);
                mainRecordSearchView.setIconified(false);
                mainRecordSearchView.setFocusable(true);
                mainRecordSearchView.requestFocusFromTouch();
            }
        });

        recordBackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordBackTV.setVisibility(View.INVISIBLE);
                recordSearchTV.setVisibility(View.VISIBLE);
                recordTitleTV.setVisibility(View.VISIBLE);
                mainRecordSearchView.clearFocus();
                mainRecordSearchView.setQuery("",false);
                mainRecordSearchView.setVisibility(View.INVISIBLE);
            }
        });

        mainRecordSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                TransactionAdapter.transactionSearch = false;
                transactionAdapter.getFilter().filter(s);
                return false;
            }
        });


        initRecyclerView();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void initRecyclerView() {



        if (getIntent().getStringExtra("userType").equals("Admin")){
            queryDisplay(getIntent().getStringExtra("userMailFromModel"), "Admin");
        }
        else {
            queryDisplay(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Agent");
        }

    }

    private void queryDisplay(String mail, String userType){
        progressBar.setVisibility(View.VISIBLE);
        Query query = FirebaseFirestore.getInstance()
                .collection("Agents/"+ mail+"/AllRecords")
                .document(getIntent().getStringExtra("recordID"))
                .collection(getIntent().getStringExtra("network")+"Transactions")
                .orderBy("dateTime", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if(task.getResult().isEmpty()){
                    nullNotice.setText("No Transaction Found!!!");
                    nullNotice.setVisibility(View.VISIBLE);
                }else{
                    transactionAdapter = new TransactionAdapter(TransactionList.this, task.getResult().toObjects(TransactionModel.class), getIntent().getStringExtra("network"), userType);
                    recordRecyclerView.setAdapter(transactionAdapter);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TransactionList.this, "Failed to load. Please retry.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void transactionDelete(int position, TransactionModel model) {

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Agents/"+ FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/AllRecords")
                .document(getIntent().getStringExtra("recordID"))
                .collection(getIntent().getStringExtra("network")+"Transactions")
                .document(model.getTransactionID());

        documentReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
        initRecyclerView();

        Snackbar.make(recordRecyclerView, "Item deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentReference.set(model);
                        initRecyclerView();
                    }
                })
                .show();

    }

    @Override
    public void transactionClick(int position, TransactionModel model) {
        Dialog dialog = new Dialog(TransactionList.this);
        dialog.setContentView(R.layout.transaction_clicked_view_layout);

        TextView tAmount = dialog.findViewById(R.id.tAmount);
        TextView tCharge = dialog.findViewById(R.id.tCharge);

        tAmount.setText(String.valueOf(model.getAmount()));
        tCharge.setText(String.valueOf(model.getCharge()));

        dialog.show();
    }
}