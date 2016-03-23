package com.michael.leancloud_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVSaveOption;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    private Button btnIncrease;

    AVObject post = new AVObject("Post");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AVOSCloud.initialize(this, "AisxjVWV6YMkayS3Tfs8gTo2-gzGzoHsz ", "ugtNpdc00e9H4RNaXDpr4img");
        //跟踪应用打开的情况
        AVAnalytics.trackAppOpened(getIntent());
        atomLike();
        btnIncrease = (Button) findViewById(R.id.increase_btn);
        btnIncrease.setOnClickListener(this);

    }

    public void test() {
        AVObject avObject = new AVObject("TestObject");
        avObject.add("key", "value");
        avObject.saveInBackground();
    }

    public void post() {
        AVObject avObject = new AVObject("Post");
        avObject.put("author2", "MichaelHe2");
        avObject.put("content2", "a new LeanCloud developer2");
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("保存成功");
                } else {
                    showToast("保存失败");
                }
            }
        });
    }

    public void query() {
        AVObject object = new AVObject("ObjectSaveWithQuery");
        object.put("int", 1);
        AVQuery query = new AVQuery("ObjectSaveWithQuery");
        query.whereEqualTo("int", 1);
        AVSaveOption option = new AVSaveOption();
        option.query(query);
        object.saveInBackground();
    }

    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void queryAsync() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Post");
        query.getInBackground("56f0b6beda2f60004cb2b711", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    showToast("获取到作者的名字为：" + avObject.getString("author2"));
                } else {
                    showToast("获取失败" + e.getMessage());
                }
            }
        });
    }

    public void updateObject() {
        String tableName = "Post";
        final AVObject post = new AVObject(tableName);
        AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);

       query.getInBackground("56f0b6beda2f60004cb2b711", new GetCallback<AVObject>() {
           @Override
           public void done(AVObject avObject, AVException e) {
               if ( e == null){
                   post.put("author2", "GodLoveMichael");
                   post.saveInBackground(new SaveCallback() {
                       @Override
                       public void done(AVException e) {
                           if (e == null) {
                               showToast("保存成功");
                           } else {
                               showToast("保存失败");
                           }
                       }
                   });
               }else{
                   showToast("后台获取失败");
               }
           }
       });
    }

    public void updateObject2(){
        AVObject post = AVObject.createWithoutData("Post","56f0b6beda2f60004cb2b711");
        post.put("author2","this is the second time to update my name,anthor way");
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("更新成功");
                } else {
                    showToast("更新失败：" + e.getMessage());
                }
            }
        });
    }

    public void atomLike(){
        post.put("like",1);
        post.saveInBackground();
    }

    public void atomIncrement(){

        post.increment("like");
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if ( e == null ){
                    showToast("增加成功");
                }else{
                    showToast("增加失败" + e.getMessage());
                }
            }
        });
    }

    /**
     * 为了减少网络传输，在更新对象操作后，对象本地的 updatedAt 字段（最后更新时间）会被刷新，但其他字段不会从云端重新获取。通过设置 fetchWhenSave 属性为 true，来获取更新字段在服务器上的最新结果，例如：
     */
    public void updateNum(){
        post.setFetchWhenSave(true);
        post.increment("like");
        final AVQuery<AVObject> query = new AVQuery<AVObject>("Post");
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                }else{
                    showToast("刷新失败" + e.getMessage()) ;
                }
            }
        });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.increase_btn:
                atomIncrement();
                break;
        }
    }
}
