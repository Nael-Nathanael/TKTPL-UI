package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.GlideApp;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.ViewCharacterActivity;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.holder.LandingCardViewHolder;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.models.TheCharacter;

public class LandingRecyclerViewAdapter extends RecyclerView.Adapter<LandingCardViewHolder> {

    private Context context;
    private List<TheCharacter> characterList;

    public LandingRecyclerViewAdapter(Context context, List<TheCharacter> characterList) {
        this.context = context;
        this.characterList = characterList;
    }

    @NonNull
    @Override
    public LandingCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_card, parent, false);
        return new LandingCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandingCardViewHolder holder, final int position) {
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
        holder.the_card.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewCharacterActivity.class);
            intent.putExtra("id", characterList.get(position).id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return characterList == null ? 0 : characterList.size();
    }
}
