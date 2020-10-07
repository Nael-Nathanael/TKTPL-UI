package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R2;

public class LandingCardViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.card_age)
    public TextView age_placement;

    @BindView(R2.id.card_name)
    public TextView name_placement;

    @BindView(R2.id.the_card)
    public CardView the_card;

    public LandingCardViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }
}
