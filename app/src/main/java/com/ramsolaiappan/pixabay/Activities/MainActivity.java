package com.ramsolaiappan.pixabay.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ramsolaiappan.pixabay.Database.DatabaseHelper;
import com.ramsolaiappan.pixabay.Dialog.NavigationDialog;
import com.ramsolaiappan.pixabay.Dialog.SearchDialog;
import com.ramsolaiappan.pixabay.GlobalVars;
import com.ramsolaiappan.pixabay.Image;
import com.ramsolaiappan.pixabay.Adapters.ImageAdapter;
import com.ramsolaiappan.pixabay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    public ArrayList<Image> imagesList = GlobalVars.imageArrayList;
    private RequestQueue requestQueue;
    private ArrayList<String> requestParams = new ArrayList<>(Arrays.asList(new String[]{"","all","all","","0","0",""}));
    protected String themeStr;
    public static DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbhelper = new DatabaseHelper(MainActivity.this);
        GlobalVars.likedImagesIdList = dbhelper.getLikedImages();

        Boolean isFirst = getSharedPreferences("settings",MODE_PRIVATE).getBoolean("isFirstTimeMain",true);
        if(isFirst)
            showNavigation();

        recyclerView = (RecyclerView) findViewById(R.id.imageRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        requestQueue = Volley.newRequestQueue(this);

        //get query when called
        if (savedInstanceState != null)
        {
            requestParams = savedInstanceState.getStringArrayList("query");
        }
        else {
            Intent intent = getIntent();
            if (intent.getStringExtra("query") == null)
                setParams("");
            else
                setParams(intent.getStringExtra("query"));
        }
        parseJson(requestParams);
    }

    private void showNavigation() {
        NavigationDialog dialog = new NavigationDialog();
        dialog.setNavDialogListener(new NavigationDialog.NavDialogListener() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }
        });
        dialog.show(getSupportFragmentManager(),"Navigation Dialog");
        getSharedPreferences("settings",MODE_PRIVATE).edit().putBoolean("isFirstTimeMain",false).apply();
    }

    private void setParams(String query)
    {
        requestParams.clear();
        requestParams.addAll(Arrays.asList(new String[]{query,"all","all","","0","0",""}));
    }

    private void parseJson(ArrayList<String> args)
    {
        if(args.size() > 0)
        {
            imagesList.clear();
            for(int page = 1; page <= 3; page++) {
                String url = "https://pixabay.com/api/?key=" + getString(R.string.api_key) + "&q=" + args.get(0) + "&image_type="+ args.get(1) + "&orientation=" + args.get(2) + "&category=" + args.get(3) + "&min_width=" + args.get(4) + "&min_height=" + args.get(5) + "&colors=" + args.get(6)+"&pretty=true&per_page=200&safesearch=true&page="+page;
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
                                boolean isLiked = false;
                                for(int l = 0;l < GlobalVars.likedImagesIdList.size(); l++)
                                {
                                    if(GlobalVars.likedImagesIdList.contains(Integer.parseInt(id)))
                                    {
                                        isLiked = true;
                                    }
                                }
                                imagesList.add(new Image(id, pageUrl, type, tags, previewUrl, imageUrl, views, downloads, likes, comments, userId, user, userImageUrl,isLiked));
                            }
                            try {
                                Collections.sort(imagesList, new Comparator<Image>() {
                                    @Override
                                    public int compare(Image o1, Image o2) {
                                        if (o1.getLikes() < o2.getLikes())
                                            return 1;
                                        else
                                            return -1;
                                    }
                                });
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            ImageAdapter imageAdapter = new ImageAdapter(MainActivity.this, R.layout.layout_listitem, imagesList);

                            imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                                @Override
                                public void OnClick(int position) {
                                    Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                                    intent.putExtra("pos",position);
                                    intent.putExtra("fromLikedActivity",false);
                                    startActivity(intent);
                                }

                                @Override
                                public void OnLiked(int position,boolean liked) {
                                    imagesList.get(position).setLiked(liked);
                                    if(liked == true && !GlobalVars.likedImagesIdList.contains(imagesList.get(position).getId())){
                                        GlobalVars.likedImagesIdList.add(Integer.parseInt(imagesList.get(position).getId()));
                                        dbhelper.addId(Integer.parseInt(imagesList.get(position).getId()));
                                    }
                                    else if(liked == false) {
                                        GlobalVars.likedImagesIdList.remove(GlobalVars.likedImagesIdList.indexOf(Integer.parseInt(imagesList.get(position).getId())));
                                        dbhelper.deleteId(Integer.parseInt(imagesList.get(position).getId()));
                                    }
                                    recyclerView.getAdapter().notifyItemChanged(position);
                                    Snackbar.make(recyclerView,"You "+ ((liked == true) ? "liked ":"unliked ") + imagesList.get(position).getUser()+"'s Image", BaseTransientBottomBar.LENGTH_SHORT).setAction("HIDE", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
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
        else
            return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        themeStr = getSharedPreferences("settings",MODE_PRIVATE).getString("theme","Light");
        switch(item.getItemId())
        {
            case R.id.search:
                SearchDialog searchDialog = new SearchDialog(MainActivity.this);
                searchDialog.setOnClickListener(new SearchDialog.OnClickListener() {
                    @Override
                    public void OnSearch(ArrayList<String> args) {
                        requestParams = args;
                        parseJson(requestParams);
                        searchDialog.dismiss();
                    }
                    @Override
                    public void OnCancel() {
                        searchDialog.dismiss();
                    }
                });
                searchDialog.show(getSupportFragmentManager(),"SearchDialog");
                break;
            case R.id.liked:
                Intent intent = new Intent(MainActivity.this,LikedImagesActivity.class);
                startActivity(intent);
                break;
            case R.id.lightTheme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                themeStr = "Light";
                break;
            case R.id.darkTheme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                themeStr = "Dark";
                break;
            case R.id.defaultTheme:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    themeStr = "Default";
                }
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.contact:
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setTitle("Contact us")
                        .setMessage("Email : ramsolaiappan@gmail.com\nPhone : +919514290441")
                        .setPositiveButton("Ok",null).show();
            case R.id.download:
                Snackbar.make(recyclerView, Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pixabay/",BaseTransientBottomBar.LENGTH_LONG).setAction("DISMISS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
        }

        getSharedPreferences("settings",MODE_PRIVATE).edit().putString("theme",themeStr).apply();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(int i = 0; i < imagesList.size(); i++)
        {
            if(GlobalVars.likedImagesIdList.contains(Integer.parseInt(imagesList.get(i).getId())))
                imagesList.get(i).setLiked(true);
            else
                imagesList.get(i).setLiked(false);
            recyclerView.getAdapter().notifyItemChanged(i);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("pos",recyclerView.getScrollY());
        outState.putStringArrayList("query",requestParams);
        super.onSaveInstanceState(outState);
    }
}
