package edu.ciit.ciit_library_app.Main_Menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.ciit.ciit_library_app.Activities.MainActivity;
import edu.ciit.ciit_library_app.Activities.ScanActivity;
import edu.ciit.ciit_library_app.Item_Decorations.Spaces_Item_Decor;
import edu.ciit.ciit_library_app.Main_Menu.RecyclerView.GenreAdapter;
import edu.ciit.ciit_library_app.Models.BookGenre;
import edu.ciit.ciit_library_app.R;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //FireBase User
    public static final String ACCOUNT = "ACCOUNT";
    public static final String PROFILEIMAGE = "PROFILEIMAGE";
    //

    //Access Firebase Database
    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final CollectionReference BookTitles = db.collection("Book_Shelf");
    CollectionReference BookGenres = db.collection("Genres");

    ///
    //Recycler View Adapter
    private RecyclerView mRecyclerView;
    private GenreAdapter mAdapter;
    //

    //Student Informations
    private String studentName;
    private String studentEmail;
    private String studentSection;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setUpRecyclerView();

        //Run Alert Dialog Containing List of Sections
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AboutDialog));
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        mBuilder.setTitle("Choose Section");

        final Spinner mSpinner = (Spinner) dialogView.findViewById(R.id.spinner_SectionsMenu);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, getResources().getStringArray(R.array.sectionList));
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        mSpinner.setAdapter(adapter);
        mBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mSpinner.getSelectedItemPosition() != 0) {
                    updateui(mSpinner.getSelectedItem().toString());
                    dialog.dismiss();
                }
            }
        });
        mBuilder.setView(dialogView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateui(String section) {

        FirebaseUser student = FirebaseAuth.getInstance().getCurrentUser();
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Uri profileImage = student.getPhotoUrl();

        TextView navStudentName = headerView.findViewById(R.id.nav_header_profileName);
        TextView navStudentSection = headerView.findViewById(R.id.nav_header_profileSection);
        ImageView navStudentImage = headerView.findViewById(R.id.nav_header_profileImage);

        Glide.with(this).load(profileImage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GLIDELOAD", "Error Load" + e.getMessage(), e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("GLIDELOAD", "Image Loaded");
                        return false;
                    }
                }).apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(16))).into(navStudentImage);

        navStudentSection.setText(studentSection);
        navStudentName.setText(student.getDisplayName());

        studentName = student.getDisplayName();
        studentEmail = student.getEmail();
        studentSection = section;
    }

    private void setUpRecyclerView() {
        Query query = BookGenres.orderBy("genre", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<BookGenre> options = new FirestoreRecyclerOptions.Builder<BookGenre>()
                .setQuery(query, BookGenre.class)
                .build();

        mAdapter = new GenreAdapter(options, getApplicationContext(), this);

        mRecyclerView = findViewById(R.id.recyclerView_MainMenu);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new Spaces_Item_Decor(18));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_browse) {
            // Handle the camera action
        } else if (id == R.id.nav_borrow) {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra("name", studentName);
            intent.putExtra("email", studentEmail);
            intent.putExtra("section", studentSection);
            startActivity(intent);

        } else if (id == R.id.nav_returned) {

        } else if (id == R.id.nav_signOut) {
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void logoutUser() {
        Intent logout = new Intent(this, MainActivity.class);
        startActivity(logout);
        finish();
    }
}
