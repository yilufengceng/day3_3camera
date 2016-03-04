package com.example.administrator.day3_3camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.client.android.Intents;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    private ImageView iv;
    private File file;
    private static  final String  TAG="print";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
    }
    public void btnClick(View v){
        switch (v.getId()){
            case R.id.bt1:
                /**
                 * 调用系统相机
                 */
                Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, 0);
                break;
            case R.id.btn2:
                Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    file = new File(Environment.getExternalStorageDirectory(),"img_"+System.currentTimeMillis()+".jpg");

                }
                Uri uri=Uri.fromFile(file);  // 图片存储的位置
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,uri);  //指定相机拍照后的保存路径
                startActivityForResult(intent2,1);

                break;
            case R.id.btn3:
                Intent intent3=new Intent(Intents.Scan.ACTION);
                int w=getResources().getDisplayMetrics().widthPixels;
                int h=getResources().getDisplayMetrics().heightPixels;
                int size=h<w?h:w;
                size=size>>1;
                intent3.putExtra(Intents.Scan.WIDTH,size);
                intent3.putExtra(Intents.Scan.HEIGHT,size);
                /**
                 * 设置扫描二维码的模式
                 */
                intent3.putExtra(Intents.Scan.MODE,Intents.Scan.QR_CODE_MODE);
                startActivityForResult(intent3,3);

                break;
            case R.id.btn4:
                break;
            case R.id.btn5:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0&&resultCode==RESULT_OK){
            Bitmap bitmap=data.getParcelableExtra("data"); //得到系统拍照的缩略图bitmap对象
            iv.setImageBitmap(bitmap);
        }else if(requestCode==1&&resultCode==RESULT_OK){
            if(file!=null&&file.exists()){
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize=2;// 压缩加载
                Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath(),options);
                iv.setImageBitmap(bitmap);
            }

        }else if(requestCode==3&&resultCode==RESULT_OK){
            String cont=data.getStringExtra(Intents.Scan.RESULT);
            Log.d(TAG,"OnActivity:  获得二维码的结果: "+cont);

        }
    }
}
