package ntu.software211.mytools;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class DesktopPictureHolder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop_picture_holder);
        CheckBox checkBox = findViewById(R.id.queryRecordBtnCheckbox);
        setSavedCheckboxState(checkBox);
        checkBox.setOnClickListener(view -> {
            SaveCheckboxState(checkBox.isChecked());
            if (checkBox.isChecked()) {
                sendBroadcastBtnVisible();
            } else {
                sendBroadcastBtnInvisible();
            }
        });
        if (!checkBox.isChecked()) {
            sendBroadcastBtnInvisible();
        } else {
            sendBroadcastBtnVisible();
        }
    }

    public void choicePicture(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
                intent.putExtra("Image",uri.toString());
                sendBroadcast(intent);
                // Toast.makeText(this, "广播已发送", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void setSavedCheckboxState(CheckBox checkBox)
    {
        SharedPreferences CheckboxState = getSharedPreferences("CheckboxState",MODE_PRIVATE);
        checkBox.setChecked(CheckboxState.getString("checkBoxState","").equals("true"));
    }
    public void SaveCheckboxState(Boolean state)
    {
        SharedPreferences.Editor CheckboxStateEditor = getSharedPreferences("CheckboxState",MODE_PRIVATE).edit();
        CheckboxStateEditor.putString("checkBoxState",state.toString());
        if(CheckboxStateEditor.commit())
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendBroadcastBtnVisible()
    {
        Intent intent = new Intent("android.appwidget.action.BTNVISIBLE");
        intent.setComponent(new ComponentName(this,MyAppWidgetProvider.class));
        sendBroadcast(intent);
    }
    public void sendBroadcastBtnInvisible()
    {
        Intent intent = new Intent("android.appwidget.action.BTNINVISIBLE");
        intent.setComponent(new ComponentName(this,MyAppWidgetProvider.class));
        sendBroadcast(intent);
        //Toast.makeText(this, "发送了取消按钮的广播", Toast.LENGTH_SHORT).show();
    }
}