package ca.keko.flickrproject.API;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ca.keko.flickrproject.R;
import ca.keko.flickrproject.SinglePhotoActivity;

public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.PhotoViewHolder> {

    // 0. VARIABLES & Constructor
    // We pass context to our adapter (e.g. our MainActivity where we call it)
    // We also pass our list of objects (photo POJO)
    private Context context;
    private List<PhotoPojo> photoPojoList;

    // Constructor
    public PhotoRecyclerAdapter(Context context, List<PhotoPojo> photoPojoList) {
        this.context = context;
        this.photoPojoList = photoPojoList;
    }

    // 1. We have an internal class PhotoViewHolder
    // that we use with our recycler cells
    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.recycler_view_cell,
                        parent,
                        false);
        return new PhotoViewHolder(view);
    }

    //-------------------------------------------------------------------------------------------//
    //  2. VIEW HOLDER
    //-------------------------------------------------------------------------------------------//

    // In our photo grid (MainActivity) we only want to display photos
    // So we only care about getting ImageView from our cell
    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCell);
        }
    }

    // 3. Get count of number of cells we want to display in our grid
    @Override
    public int getItemCount(){
        return photoPojoList.size();
    }

    // 4. When we bind our view holder, we place our current image into the cell
    // Parameters: holder (for our ImageView), position (current position in list)
    // Uses GLIDE to display image from String URL, which we grab from photo object
    @Override
    public void onBindViewHolder(
            PhotoViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        PhotoPojo photoPojo = photoPojoList.get(position);
        String imageURL = photoPojo.imageURL;

        // Use Glide to update ImageView, with our adapter's context
        Glide
            .with(context)
            .load(imageURL)
            .into(imageView);

        setOnClickRecyclerCell(holder, photoPojo);
    }

    private void setOnClickRecyclerCell(
            PhotoViewHolder holder, PhotoPojo photo
    ) {
        holder.itemView.setOnClickListener(view -> {
            // Instead of passing photoURL as a String to our details activity
            // We want to pass the bitmap since we've already loaded our image from URL
            // SOURCE: https://stackoverflow.com/questions/26865787/get-bitmap-from-imageview-in-android-l
            ImageView imageView = holder.imageView;
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            // Intent to send to our details activity: SinglePhotoActivity
            Intent intent = new Intent(view.getContext(), SinglePhotoActivity.class);

            // Need to create a Bundle because I want to pass both the PhotoPojo object
            // and the Bitmap to our SinglePhotoActivity
            // PhotoPojo implements Serializable so that I can use putSerializable to send it
            // Bundle, however, does not implement Serializable and instead is Parcelable

            int position = holder.getAbsoluteAdapterPosition();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putSerializable("photoList", (ArrayList<PhotoPojo>) photoPojoList);
            intent.putExtra("photoBundle", bundle);

            // Putting startActivity() in a try/catch block for safety
            // So app doesn't crash if there's a mistake
            try {
                view.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
