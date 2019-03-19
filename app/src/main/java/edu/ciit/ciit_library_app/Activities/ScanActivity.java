package edu.ciit.ciit_library_app.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import edu.ciit.ciit_library_app.Main_Menu.MainMenu;
import edu.ciit.ciit_library_app.Models.BookShelf;
import edu.ciit.ciit_library_app.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference BookBorrowed = db.collection("Book_Borrowed");
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();
        MainMenu.BookTitles.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (final QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    final BookShelf bookShelf = queryDocumentSnapshot.toObject(BookShelf.class);
                    int bookId = (bookShelf.getId());
                    if (Integer.toString(bookId).equals(result.getText())) {

                        BookShelf sendBorrowedBook = new BookShelf(bookShelf.getTitle(), bookShelf.getGenre(), bookShelf.getId());

                        BookBorrowed.add(sendBorrowedBook).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                MainMenu.BookTitles.document(queryDocumentSnapshot.getId()).delete();
                                Toast.makeText(ScanActivity.this, bookShelf.getTitle() + "Borrowed", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ScanActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        onBackPressed();
    }
}
