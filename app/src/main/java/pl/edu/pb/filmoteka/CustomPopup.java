package pl.edu.pb.filmoteka;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

public class CustomPopup {

    public static void showRandomFilmPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);


        TextView textView = new TextView(context);
        textView.setText(context.getString(R.string.rand_film_popup));

        Resources resources = context.getResources();
        int color = resources.getColor(R.color.colorPrimary, null);

        textView.setTextSize(24);
        textView.setTextColor(color);
        textView.setGravity(Gravity.CENTER);

        linearLayout.addView(textView);

        Space space = new Space(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                50));
        linearLayout.addView(space);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.mobile_phone);
        int imageSize = dpToPx(context, 100);
        int padding = dpToPx(context, 16); // Dodaj dowolny padding w pikselach
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
        imageView.setLayoutParams(params);
        imageView.setPadding(padding, padding, padding, padding);

        linearLayout.addView(imageView);

        builder.setView(linearLayout);

        builder.setPositiveButton((context.getString(R.string.back)), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        Animation animation = new RotateAnimation(0, 30, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(500);

        imageView.startAnimation(animation);

        dialog.show();
    }
    private static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}