package edu.ciit.ciit_library_app.Main_Menu.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import edu.ciit.ciit_library_app.Item_Decorations.Spaces_Item_Decor;
import edu.ciit.ciit_library_app.Main_Menu.RecyclerView.TitleAdapter;
import edu.ciit.ciit_library_app.Models.BookShelf;
import edu.ciit.ciit_library_app.R;

public class TitleCardView extends Fragment {

    private static final String ARG_TEXT = "arg_text";
    private String TAG = "FIRESTORE";

    //FirebaseUI
    private RecyclerView mRecyclerView;
    private TitleAdapter mAdapter;
    //

    //FireStore Database
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference BookTitles = db.collection("Book_Shelf");
    Query query;
    //
    private String bookGenre;

    public static TitleCardView newInstance(String genre) {
        TitleCardView fragment = new TitleCardView();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, genre);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_title_card_view, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerView_BookItems);

        bookGenre = getArguments().getString("arg_text");

        query = BookTitles.whereEqualTo("genre", bookGenre).orderBy("title");
        setUpRecyclerView(query);

        BookTitles.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen Failed", e);
                } else {
                    Log.d(TAG, "Listen Success!");
                }
            }
        });
        return v;
    }

    private void setUpRecyclerView(Query query) {

        FirestoreRecyclerOptions<BookShelf> options = new FirestoreRecyclerOptions.Builder<BookShelf>()
                .setQuery(query, BookShelf.class)
                .build();

        mAdapter = new TitleAdapter(options, getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new Spaces_Item_Decor(70));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}
