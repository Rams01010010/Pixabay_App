package com.ramsolaiappan.pixabay.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ramsolaiappan.pixabay.GlobalVars;
import com.ramsolaiappan.pixabay.Image;
import com.ramsolaiappan.pixabay.R;
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    ImageView userImage,image;
    TextView imageId,type,views,downloads,likes,comments,pageUrl,userId,userName;
    Chip chip1,chip2,chip3;
    FloatingActionButton fab;
    boolean isFrontCard = true;
    int position = 0;


    LinearLayout cardImageLayout,cardDetailsLayout;
    CardView cardView;
    Image clickedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Boolean isFirst = getSharedPreferences("settings",MODE_PRIVATE).getBoolean("isFirstTimeDetail",true);
        if(isFirst)
            showNavigation();

        cardView =  findViewById(R.id.card);
        cardImageLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_image,cardView,false);
        cardDetailsLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_imagedetails,cardView,false);
        fab = findViewById(R.id.floatingActionButton);

        cardView.addView(cardImageLayout);

        //get ClickedImage's Position
        try {
            Intent intent = getIntent();
            position = intent.getIntExtra("pos",0);
            if(intent.getBooleanExtra("fromLikedActivity",false))
                clickedImage = GlobalVars.likedImagesList.get(position);
            else
                clickedImage = GlobalVars.imageArrayList.get(position);
            if(GlobalVars.likedImagesIdList.contains(Integer.parseInt(clickedImage.getId())))
                fab.setImageResource(R.drawable.ic_like_fill);
            else
                fab.setImageResource(R.drawable.ic_like_outline);
            prepareCard(clickedImage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean liked = !clickedImage.isLiked();
                clickedImage.setLiked(liked);
                if(clickedImage.isLiked() == true && !GlobalVars.likedImagesIdList.contains(clickedImage.getId())) {
                    GlobalVars.likedImagesIdList.add(Integer.parseInt(clickedImage.getId()));
                    MainActivity.dbhelper.addId(Integer.parseInt(clickedImage.getId()));
                }
                else if(clickedImage.isLiked() == false) {
                    GlobalVars.likedImagesIdList.remove(GlobalVars.likedImagesIdList.indexOf(Integer.parseInt(clickedImage.getId())));
                    MainActivity.dbhelper.deleteId(Integer.parseInt(clickedImage.getId()));
                }
                fab.setImageResource((liked == true) ? R.drawable.ic_like_fill : R.drawable.ic_like_outline);
                Snackbar.make(fab,"You "+ ((liked == true) ? "liked ":"unliked ") + GlobalVars.imageArrayList.get(position).getUser()+"'s Image", BaseTransientBottomBar.LENGTH_SHORT).setAction("HIDE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {}
                }).show();
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFrontCard == true){
                    animateCard(cardDetailsLayout,90);
                    isFrontCard = false;
                }
                else {
                    animateCard(cardImageLayout,-90);
                    isFrontCard = true;
                }
            }
        });
    }

    private void showNavigation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Get To Know")
                .setMessage("Click the Image to see Image's Details")
                .setPositiveButton("Got it",null);
        builder.show();
        getSharedPreferences("settings",MODE_PRIVATE).edit().putBoolean("isFirstTimeDetail",false).apply();
    }

    public void prepareCard(Image clickedImage)
    {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        float distance = cardView.getCameraDistance() * (scale+(scale/2));
        cardView.setCameraDistance(distance);

        image = (ImageView) cardImageLayout.findViewById(R.id.imageIVD);
        userImage = (ImageView) cardDetailsLayout.findViewById(R.id.userImageIVD);
        imageId = (TextView) cardDetailsLayout.findViewById(R.id.idTVD);
        pageUrl = (TextView) cardDetailsLayout.findViewById(R.id.pageUrlTVD);
        type = (TextView) cardDetailsLayout.findViewById(R.id.typeTVD);
        views = (TextView) cardDetailsLayout.findViewById(R.id.viewsTVD);
        downloads = (TextView) cardDetailsLayout.findViewById(R.id.downloadsTVD);
        likes = (TextView) cardDetailsLayout.findViewById(R.id.likesTVD);
        comments = (TextView) cardDetailsLayout.findViewById(R.id.commentsTVD);
        userId = (TextView) cardDetailsLayout.findViewById(R.id.userIdTVD);
        userName = (TextView) cardDetailsLayout.findViewById(R.id.userNameTVD);
        chip1 = (Chip) cardDetailsLayout.findViewById(R.id.chip1);
        chip2 = (Chip) cardDetailsLayout.findViewById(R.id.chip2);
        chip3 = (Chip) cardDetailsLayout.findViewById(R.id.chip3);


        if(!clickedImage.getImageUrl().equals(""))
        Picasso.get().load(clickedImage.getImageUrl()).fit().centerInside().into(image);
        if(!clickedImage.getUserImageUrl().equals(""))
        Picasso.get().load(clickedImage.getUserImageUrl()).transform(new GlobalVars.CircleTransform()).into(userImage);
        imageId.setText(clickedImage.getId());
        type.setText(clickedImage.getType());
        views.setText(String.valueOf(clickedImage.getViews()));
        downloads.setText(String.valueOf(clickedImage.getDownloads()));
        likes.setText(String.valueOf(clickedImage.getLikes()));
        comments.setText(String.valueOf(clickedImage.getComments()));
        pageUrl.setText(clickedImage.getPageUrl());
        userId.setText(clickedImage.getUserId());
        userName.setText(clickedImage.getUser());
        String[] tags = clickedImage.getTags().split(",");
        chip1.setText(tags[0]);
        chip2.setText(tags[1]);
        chip3.setText(tags[2]);

        pageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(clickedImage.getPageUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        chip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,MainActivity.class);
                intent.putExtra("query",chip1.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        chip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,MainActivity.class);
                intent.putExtra("query",chip2.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        chip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,MainActivity.class);
                intent.putExtra("query",chip3.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void animateCard(LinearLayout resource,int deg)
    {
        cardView.setEnabled(false);
        cardView.animate().rotationYBy(-(deg)).setDuration(250).withEndAction(new Runnable() {
            @Override
            public void run() {
                cardView.removeAllViews();
                cardView.addView(resource);
                cardView.setRotationY(deg);
                cardView.animate().rotationYBy(-(deg)).setDuration(250);
                cardView.setEnabled(true);
            }
        });
    }

    public void checkPermissions(View view)
    {
        if(ContextCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            downloadImage();
        else
            ActivityCompat.requestPermissions(DetailActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                downloadImage();
            else
                Toast.makeText(DetailActivity.this, "Allow permission to proceed", Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void downloadImage()
    {
        try {
            Bitmap bitmap = new getImage().execute(clickedImage.getImageUrl()).get();
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pixabay/");
            dir.mkdir();
            File file = new File(dir,"Image - "+clickedImage.getId()+".jpeg");
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(DetailActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void shareImage(View view)
    {
        downloadImage();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pixabay/"+"Image - "+clickedImage.getId()+".jpeg"));
        startActivity(Intent.createChooser(share,"Share Iamge"));
    }

    public class getImage extends AsyncTask<String,Void,Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(clickedImage.getImageUrl().toLowerCase());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                Bitmap bitmap = ((BitmapDrawable) getDrawable(R.drawable.nodp)).getBitmap();
                return bitmap;
            }
        }
    }
}