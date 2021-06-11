package com.foresthero.downloadapkdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.BuildConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    static String url = "http://gdown.baidu.com/data/wisegame/57a788487345e938/QQ_358.apk";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);



    }

    public void click(View view) {
        downlaod();
    }

    private void downlaod() {
        final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download";
        final String fileName = "QQ_358.apk";
        File file = new File(filePath + "/" + fileName);
        if (file.exists())
            file.delete();
        OkGo.<File>get(url).execute(new FileCallback(filePath, fileName) {

            @Override
            public void onStart(Request<File, ? extends Request> request) {
                super.onStart(request);
                Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);

                tv.setText(progress.currentSize + "/" + progress.totalSize);
            }

            @Override
            public void onSuccess(Response<File> response) {
                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(filePath + "/" + fileName);
                Uri uri;
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".MyFileProvider", file);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    uri = Uri.fromFile(file);
                }
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                startActivity(intent);

            }
        });
    }


}