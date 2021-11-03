package com.ramsolaiappan.pixabay.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ramsolaiappan.pixabay.GlobalVars;
import com.ramsolaiappan.pixabay.Image;
import com.ramsolaiappan.pixabay.Adapters.ImageAdapter;
import com.ramsolaiappan.pixabay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LikedImagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public ArrayList<Image> likedImagesList = GlobalVars.likedImagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_images);

        getSupportActionBar().setTitle("Liked Images");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.likedImageRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        likedImagesList.clear();
        for(int i = 0; i < GlobalVars.likedImagesIdList.size(); i++)
        {
            String url = "https://pixabay.com/api/?key=" + getString(R.string.api_key) + "&id=" + GlobalVars.likedImagesIdList.get(i).toString();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("hits");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hit = jsonArray.getJSONObject(i);
                            String id = String.valueOf(hit.getInt("id"));
                            String pageUrl = hit.getString("pageURL");
                            String type = hit.getString("type");
                            String tags = hit.getString("tags");
                            String previewUrl = hit.getString("previewURL");
                            String imageUrl = hit.getString("largeImageURL");
                            int views = hit.getInt("views");
                            int downloads = hit.getInt("downloads");
                            int likes = hit.getInt("likes");
                            int comments = hit.getInt("comments");
                            String userId = String.valueOf(hit.getInt("user_id"));
                            String user = hit.getString("user");
                            String userImageUrl = hit.getString("userImageURL");
                            likedImagesList.add(new Image(id, pageUrl, type, tags, previewUrl, imageUrl, views, downloads, likes, comments, userId, user, userImageUrl,true));
                        }
                        ImageAdapter imageAdapter = new ImageAdapter(LikedImagesActivity.this, R.layout.layout_listitem, likedImagesList);

                        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                            @Override
                            public void OnClick(int position) {
                                Intent intent = new Intent(LikedImagesActivity.this,DetailActivity.class);
                                intent.putExtra("pos",position);
                                intent.putExtra("fromLikedActivity",true);
                                startActivity(intent);
                            }

                            @Override
                            public void OnLiked(int position,boolean liked) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LikedImagesActivity.this);
                                builder.setTitle("Warning")
                                        .setMessage("You don't like this?")
                                        .setPositiveButton("No",null)
                                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                for(int i = 0; i < GlobalVars.imageArrayList.size(); i++)
                                                {
                                                    if(GlobalVars.imageArrayList.get(i).getId().equals(likedImagesList.get(position).getId()))
                                                    {
                                                        GlobalVars.imageArrayList.get(i).setLiked(false);
                                                    }
                                                }
                                                MainActivity.dbhelper.deleteId(GlobalVars.likedImagesIdList.get(position));
                                                GlobalVars.likedImagesIdList.remove(position);
                                                Snackbar.make(recyclerView,"You unliked "+likedImagesList.get(position).getUser()+"'s Image", BaseTransientBottomBar.LENGTH_SHORT).setAction("HIDE", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                    }
                                                }).show();
                                                likedImagesList.remove(position);
                                                recyclerView.getAdapter().notifyItemRemoved(position);
                                            }
                                        }).show();
                            }
                        });
                        recyclerView.setAdapter(imageAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(int i = 0; i < likedImagesList.size();i++)
        {
            if(!GlobalVars.likedImagesIdList.contains(Integer.parseInt(likedImagesList.get(i).getId())))
            {
                likedImagesList.remove(i);
                recyclerView.getAdapter().notifyItemRemoved(i);
            }
        }
    }
}