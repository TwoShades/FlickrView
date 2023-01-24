package ca.keko.flickrproject.API;

import android.os.Parcelable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoPojo implements Serializable {
    // POJO = Plain Old Java Object

    String id;
    String title;
    String dateTaken;
    String imageURL;

    public PhotoPojo(String id, String title, String dateTaken, String imageURL) {
        this.id = id;
        this.title = title;
        this.dateTaken = dateTaken;
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "PhotoPojo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dateTaken='" + dateTaken + '\'' +
                ", photoURL='" + imageURL + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if (this.title.isEmpty())
            return "Untitled";
        else
            return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTaken() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(this.dateTaken);
            formatter.applyPattern("MMMM d, yyyy");
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this.dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
