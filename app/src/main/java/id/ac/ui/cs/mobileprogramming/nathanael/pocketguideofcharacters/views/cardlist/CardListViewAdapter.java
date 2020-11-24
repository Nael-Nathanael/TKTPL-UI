package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.cardlist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R2;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.utility.dependency_injectors.GlideApp;

public class CardListViewAdapter extends RecyclerView.Adapter<CardListViewAdapter.CardListViewHolder> {

    private Context context;
    private List<TheCharacter> characterList;

    public CardListViewAdapter(Context context, List<TheCharacter> characterList) {
        this.context = context;
        this.characterList = characterList;
    }

    @NonNull
    @Override
    public CardListViewAdapter.CardListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_card, parent, false);
        return new CardListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardListViewAdapter.CardListViewHolder holder, final int position) {
        GlideApp
                .with(context)
                .load(characterList.get(position).image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.card_image_loading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.card_image_loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.image_placement);
        holder.name_placement.setText(characterList.get(position).name);
        holder.character_id.setText(characterList.get(position).id);
    }

    @Override
    public int getItemCount() {
        return characterList == null ? 0 : characterList.size();
    }

    static class CardListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.card_image)
        public ImageView image_placement;

        @BindView(R2.id.character_id)
        public TextView character_id;

        @BindView(R2.id.card_name)
        public TextView name_placement;

        @BindView(R2.id.the_card)
        public CardView the_card;

        @BindView(R2.id.card_image_loading)
        public ProgressBar card_image_loading;

        public CardListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
