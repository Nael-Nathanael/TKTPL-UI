package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;

public class LandingCardViewHolder extends RecyclerView.ViewHolder {
    private static final int CARD_WIDTH_ASPECT_RATIO = 3;
    private static final int CARD_HEIGHT_ASPECT_RATIO = 4;
    public TextView age_placement;
    public TextView name_placement;
    public CardView the_card;

    public LandingCardViewHolder(@NonNull View itemView) {
        super(itemView);

        the_card = itemView.findViewById(R.id.the_card);
        age_placement = itemView.findViewById(R.id.card_age);
        name_placement = itemView.findViewById(R.id.card_name);
    }
}
