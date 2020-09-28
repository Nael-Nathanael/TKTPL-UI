package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.R;

public class LandingCardViewHolder extends RecyclerView.ViewHolder {
    public TextView age_placement;
    public TextView name_placement;
    public CardView the_card;

    public LandingCardViewHolder(@NonNull View itemView) {
        super(itemView);

        the_card = (CardView) itemView.findViewById(R.id.the_card);
        age_placement = (TextView) itemView.findViewById(R.id.card_age);
        name_placement = (TextView) itemView.findViewById(R.id.card_name);
    }
}
