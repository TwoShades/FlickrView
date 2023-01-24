package ca.keko.flickrproject;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import ca.keko.flickrproject.API.PhotoPojo;
import ca.keko.flickrproject.databinding.DetailsActivityBinding;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SinglePhotoActivity extends AppCompatActivity {

    private DetailsActivityBinding binding;

    ImageView imageViewDetails;
    TextView textViewTitle, textViewDateTaken;

    ArrayList<PhotoPojo> photoList;
    int currentPhotoIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DetailsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.fabNext.setOnClickListener(view -> nextPhoto());
        binding.fabPrev.setOnClickListener(view -> prevPhoto());

        initialize();
        receiveIntent();
        updatePhotoMetaData();
    }

    private void initialize() {
        photoList = new ArrayList<>();
        imageViewDetails =  findViewById(R.id.imageViewDetails);
        textViewTitle =     findViewById(R.id.textViewTitle);
        textViewDateTaken = findViewById(R.id.textViewDateTaken);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("photoBundle");
        currentPhotoIndex = bundle.getInt("position");
        photoList = (ArrayList<PhotoPojo>) bundle.getSerializable("photoList");
    }

    public void nextPhoto() {
        if(photoList != null && photoList.size() > 0) {
            try {
                currentPhotoIndex++;
                // Modulo: if index goes out of bounds, will be reset to 0
                // i.e.: wraps around back to beginning when we reach end of photo list
                currentPhotoIndex %= photoList.size();
                updatePhotoMetaData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void prevPhoto() {
        if(photoList != null && photoList.size() > 0) {
            try {
                if(currentPhotoIndex <= 0)
                    currentPhotoIndex = (photoList.size() - 1);
                else
                    currentPhotoIndex--;
                updatePhotoMetaData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePhotoMetaData() {
        PhotoPojo currentPhoto = photoList.get(currentPhotoIndex);

        String photoCounter = "\nPhoto " + (currentPhotoIndex+1) + " of " + photoList.size();
        String dateTakenAndCounter = currentPhoto.getDateTaken() + photoCounter;

        textViewTitle.setText(currentPhoto.getTitle());
        textViewDateTaken.setText(dateTakenAndCounter);
        textViewDateTaken.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Using Glide:
        String photoURL = currentPhoto.getImageURL();
        Glide.with(this)
                .load(photoURL)
                .into(imageViewDetails);
    }
}