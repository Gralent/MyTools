package ntu.software211.mytools;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.IOException;

public class MyAppWidgetProvider extends AppWidgetProvider   {
    private static final String WIDGET_BTN_ACTION = "android.appwidget.action.QUERYRECORD";
    private static final String WIDGET_BTN_VISIBLE = "android.appwidget.action.BTNVISIBLE";
    private static final String WIDGET_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    private static final String WIDGET_BTN_INVISIBLE = "android.appwidget.action.BTNINVISIBLE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);//需要构造一个RemoteViews
        Intent intent = new Intent();
        intent.setClass(context, MyAppWidgetProvider.class); //通过intent把广播发给TestWidget本身，TestWidget接受到广播之后，会调用onReceive()方法进而刷新界面。
        intent.setAction(WIDGET_BTN_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        remoteViews.setOnClickPendingIntent(R.id.queryRecord, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent != null && TextUtils.equals(intent.getAction(), WIDGET_BTN_ACTION)){
            // Toast.makeText(context, "收到广播WIDGET_BTN_ACTION", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("https://cydj03.weiynet.cn/#/pages/user-report/user-report");
            Intent urlIntent = new Intent(Intent.ACTION_VIEW, uri);
            urlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(urlIntent);
        }
        else if (TextUtils.equals(intent.getAction(), WIDGET_UPDATE_ACTION)){
            //Toast.makeText(context, "收到广播WIDGET_UPDATE_ACTION", Toast.LENGTH_SHORT).show();
            try {
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);//需要构造一个RemoteViews
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(intent.getStringExtra("Image")));
                remoteViews.setImageViewBitmap(R.id.heldPicture,bitmap);
                Toast.makeText(context, "图片已更新", Toast.LENGTH_SHORT).show();
                AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidgetProvider.class),remoteViews);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(TextUtils.equals(intent.getAction(), WIDGET_BTN_INVISIBLE)){
            //Toast.makeText(context, "收到广播WIDGET_BTN_INVISIBLE", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "取消按钮", Toast.LENGTH_SHORT).show();
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);//需要构造一个RemoteViews
            remoteViews.setViewVisibility(R.id.queryRecord, View.INVISIBLE);
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidgetProvider.class),remoteViews);
        }
        else if(TextUtils.equals(intent.getAction(), WIDGET_BTN_VISIBLE))
        {
            //Toast.makeText(context, "收到广播WIDGET_BTN_VISIBLE", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "显示按钮", Toast.LENGTH_SHORT).show();
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);//需要构造一个RemoteViews
            remoteViews.setViewVisibility(R.id.queryRecord, View.VISIBLE);
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidgetProvider.class),remoteViews);
        }
    }
}
