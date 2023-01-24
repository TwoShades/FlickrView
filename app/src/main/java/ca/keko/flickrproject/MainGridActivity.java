package ca.keko.flickrproject;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ca.keko.flickrproject.API.FlickrAPI;
import ca.keko.flickrproject.API.PhotoPojo;
import ca.keko.flickrproject.API.PhotoRecyclerAdapter;
import ca.keko.flickrproject.databinding.DetailsActivityBinding;
import ca.keko.flickrproject.databinding.RecyclerViewBinding;

public class MainGridActivity extends AppCompatActivity {

    RecyclerViewBinding binding;

    RecyclerView recyclerView;
    PhotoRecyclerAdapter recyclerView_adapter;
    private List<PhotoPojo> photoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        initializePhotoList();
    }

    private void initializePhotoList() {
        FlickrAPI flickrAPI = new FlickrAPI(this);
        flickrAPI.fetchInterestingPhotos();
    }

    // Method called in AsyncTask onPostExecute to get our photo list
    public void receivedPhotos(List<PhotoPojo> photoList) {
        this.photoList = photoList;
    }

    // Initializing our RecyclerView and setLayoutManager should both
    // be done in onPostExecute for now
    public void initializeRecyclerView() {
        recyclerView = binding.recyclerViewPhotoGrid;
        recyclerView_adapter = new PhotoRecyclerAdapter(
                this,
                (ArrayList<PhotoPojo>) photoList);
        recyclerView.setAdapter(recyclerView_adapter);
    }

    // Here we can specify we want to use a grid layout
    public void setRecyclerViewLayoutManager() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }
}
