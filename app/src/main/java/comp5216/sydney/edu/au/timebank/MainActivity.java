package comp5216.sydney.edu.au.timebank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Collections;

import comp5216.sydney.edu.au.timebank.adapter.TaskAdapter;
import comp5216.sydney.edu.au.timebank.model.Task;
import comp5216.sydney.edu.au.timebank.viewmodel.MainActivityViewModel;
import comp5216.sydney.edu.au.timebank.R;
import comp5216.sydney.edu.au.timebank.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskSelectedListener{


    private static final String TAG = "MainActivity";


    private MainActivityViewModel mViewModel;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private static final int LIMIT = 50;
    private TaskAdapter taskAdapter;

    private RecyclerView tasksRecycler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tasksRecycler=findViewById(R.id.recycler_tasks);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        initFirestore();
        initRecyclerView();


    }


    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
        // Get the 50 highest rated restaurants
        mQuery = mFirestore.collection("tasks")
                .orderBy("title", Query.Direction.DESCENDING)
                .limit(LIMIT);
    }

    private void initRecyclerView(){

        if (mQuery == null) {
        Log.w(TAG, "No query, not initializing RecyclerView");
    }

        taskAdapter = new TaskAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    tasksRecycler.setVisibility(View.GONE);

                } else {
                    tasksRecycler.setVisibility(View.VISIBLE);

                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        tasksRecycler.setLayoutManager(new LinearLayoutManager(this));
        tasksRecycler.setAdapter(taskAdapter);
    }



    public void onAddItemsClicked() {
        // Get a reference to the restaurants collection
        CollectionReference tasks = mFirestore.collection("tasks");

            // Get a random Restaurant POJO
            Task task = new Task("1","22","info1","44",4);
        Task task2 = new Task("2","22","info12","44",4511);
        Task task3 = new Task("3","22","info123","44",415);
        Task task4 = new Task("4","22","info1234","44",458);
        Task task5 = new Task("5","22","info1234","44",456);

            // Add a new document to the restaurants collection
         tasks.add(task);
        tasks.add(task2);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task5);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (shouldStartSignIn()) {
            //load sign in page
            startSignIn();
            return;
        }
        if (taskAdapter != null) {
            taskAdapter.startListening();
        }
        }

    @Override
    public void onStop() {
        super.onStop();
        if (taskAdapter != null) {
            taskAdapter.stopListening();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_items:
                onAddItemsClicked();
                break;
            case R.id.menu_sign_out:
                AuthUI.getInstance().signOut(this);
                startSignIn();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private boolean shouldStartSignIn() {
        //chek firebase authorization
        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true);
    }


    @Override
    public void onTaskSelected(DocumentSnapshot task) {

    }

//    @Override
//    public void onRestaurantSelected(DocumentSnapshot restaurant) {
//        // Go to the details page for the selected restaurant
//        Intent intent = new Intent(this, RestaurantDetailActivity.class);
//        intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_ID, restaurant.getId());
//
//        startActivity(intent);
//    }

}