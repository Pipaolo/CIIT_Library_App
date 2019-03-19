package edu.ciit.ciit_library_app.Main_Menu.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import edu.ciit.ciit_library_app.Main_Menu.MainMenu;
import edu.ciit.ciit_library_app.Main_Menu.fragments.TitleCardView;
import edu.ciit.ciit_library_app.Models.BookGenre;
import edu.ciit.ciit_library_app.R;

public class GenreAdapter extends FirestoreRecyclerAdapter<BookGenre, GenreAdapter.GenreHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    MainMenu activity;
    private String TAG = "FIRESTORE";

    public GenreAdapter(@NonNull FirestoreRecyclerOptions<BookGenre> options, Context context, MainMenu activity) {
        super(options);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull final GenreHolder holder, int position, @NonNull BookGenre model) {
        final String bookGenre = model.getGenre();
        holder.bookGenre.setText(model.getGenre());

        holder.itemView.findViewById(R.id.cardView_Buttons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(context, bookGenre, Toast.LENGTH_SHORT).show();
                TitleCardView fragment = TitleCardView.newInstance(bookGenre);

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_out_bottom, R.anim.slide_in_button)
                        .replace(R.id.main_menu, fragment)
                        .addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public GenreHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_bookgenre, viewGroup, false);

        return new GenreHolder(v);
    }

    class GenreHolder extends RecyclerView.ViewHolder {
        TextView bookGenre;

        public GenreHolder(View itemView) {
            super(itemView);
            bookGenre = itemView.findViewById(R.id.cardView_BookGenre);

        }

    }


}
