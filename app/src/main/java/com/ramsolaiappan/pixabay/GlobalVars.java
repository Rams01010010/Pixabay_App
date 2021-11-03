package com.ramsolaiappan.pixabay;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.provider.ContactsContract;

import com.ramsolaiappan.pixabay.Activities.MainActivity;
import com.ramsolaiappan.pixabay.Database.DatabaseHelper;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class GlobalVars {
    public static ArrayList<Image> imageArrayList = new ArrayList<>();
    public static ArrayList<Image> likedImagesList = new ArrayList<>();
    public static ArrayList<Integer> likedImagesIdList = new ArrayList<>();
    public static boolean isLiked(int id)
    {
        boolean liked = false;
        for(int i = 0; i < GlobalVars.likedImagesIdList.size(); i++)
        {
            if(id == GlobalVars.likedImagesIdList.get(i))
            {
                liked = true;
            }
        }
        return liked;
    }
    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
