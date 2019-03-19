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

import edu.ciit.ciit_library_app.Models.BookShelf;
import edu.ciit.ciit_library_app.R;

public class TitleAdapter extends FirestoreRecyclerAdapter<BookShelf, TitleAdapter.TitleHolder> {

    Context mContext;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TitleAdapter(@NonNull FirestoreRecyclerOptions<BookShelf> options, Context context) {
        super(options);
        this.mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull TitleHolder holder, int position, @NonNull BookShelf model) {
        final String bookTitle = model.getTitle();

        holder.textViewBookTitle.setText(model.getTitle());
        holder.textViewBookGenre.setText(model.getGenre());
        holder.itemView.findViewById(R.id.cardView_BookTitles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, bookTitle, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @NonNull
    @Override
    public TitleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_booktitles, viewGroup, false);
        return new TitleHolder(v);
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        TextView textViewBookGenre;
        TextView textViewBookTitle;

        public TitleHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookTitle = itemView.findViewById(R.id.textViewBookTitle);
            textViewBookGenre = itemView.findViewById(R.id.textViewBookGenre);
        }
    }
}
