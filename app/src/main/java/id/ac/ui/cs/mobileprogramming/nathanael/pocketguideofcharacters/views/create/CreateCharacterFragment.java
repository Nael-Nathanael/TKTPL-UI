package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.create;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R2;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.utility.dependency_injectors.GlideApp;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.viewmodels.NavigationViewModel;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.utility.firebase.FirebaseConnectorService;

import static android.app.Activity.RESULT_OK;

public class CreateCharacterFragment extends Fragment {

    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseConnectorService firebaseConnectorService;
    @BindView(R2.id.nameField)
    TextView nameField;
    @BindView(R2.id.ageField)
    TextView ageField;
    @BindView(R2.id.createButton)
    Button createButton;
    @BindView(R2.id.image_input)
    ImageView image_input;
    @BindView(R2.id.image_cell)
    ConstraintLayout image_cell;
    @BindView(R2.id.image_hint)
    TextView image_hint;
    @BindView(R2.id.load_pg_bar)
    ProgressBar load_pg_bar;
    Uri filepath;
    FirebaseStorage storage;
    StorageReference storageReference;
    View root;
    private boolean haveImage = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_create, container, false);
        firebaseConnectorService = new FirebaseConnectorService();
        ButterKnife.bind(this, root);

        Uri iconuri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.raw.icon);
        GlideApp.with(this).load(iconuri).into(image_input);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        createButton.setOnClickListener(v -> createButtonClick());

        image_cell.setOnClickListener(v -> SelectImage());

        setHasOptionsMenu(true);

        return root;
    }

    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filepath = data.getData();

            load_pg_bar.setVisibility(View.VISIBLE);
            GlideApp
                    .with(this)
                    .load(filepath)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            load_pg_bar.setVisibility(View.GONE);
                            haveImage = false;
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            load_pg_bar.setVisibility(View.GONE);
                            haveImage = true;
                            return false;
                        }
                    })
                    .into(image_input);

            image_hint.setVisibility(View.GONE);
        }
    }

    public void createButtonClick() {
        String name = nameField.getText().toString();
        String ageStr = ageField.getText().toString();

        if (!name.isEmpty() && !ageStr.isEmpty() && haveImage) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filepath)
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                progressDialog.dismiss();
                                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                                    int age = Integer.parseInt(ageStr);
                                    firebaseConnectorService.createCharacter(name, age, uri.toString());
                                    reset_fragment_state();
                                    Toast.makeText(getContext(), "Character Creation Complete", Toast.LENGTH_SHORT).show();

                                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                                });

                            })
                    .addOnFailureListener(e -> progressDialog.dismiss())
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            });
        }
    }

    private void reset_fragment_state() {
        nameField.setText("");
        ageField.setText("");
        haveImage = false;
        Uri iconuri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.raw.icon);
        GlideApp.with(this).load(iconuri).into(image_input);
        image_hint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.custom_timer_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Create");

        NavigationViewModel navigationViewModel = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        navigationViewModel.getActive().setValue(false);
    }
}
