package com.srisbeauty.srisbeauty.model.ishan387.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.srisbeauty.srisbeauty.R;
import com.srisbeauty.srisbeauty.model.BadgeDrawable;

/**
 * Created by ishan on 09-01-2018.
 */

public class Util
{
    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null)
            {
                for(int i=0 ; i<info.length;i++)
                {
                    if(info[i].getState()== NetworkInfo.State.CONNECTED);
                    {
                        return  true;
                    }
                }
            }
        }
        return false;
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);

    }
}
