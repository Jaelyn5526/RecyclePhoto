package jaelyn.blg;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {

    private RecyclerView recyclerView;
    private PhotoAdapter recycleAdapter;
    private ArrayList<PhotoDataBean> datas;
    private TextView chooseTV, delectTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseTV = (TextView) findViewById(R.id.choose_tv);
        chooseTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delectTv.isShown()){
                    chooseTV.setText("选择");
                    delectTv.setVisibility(View.GONE);
                    recycleAdapter.isOpenSelectMode(false);
                }else {
                    chooseTV.setText("取消");
                    delectTv.setVisibility(View.VISIBLE);
                    recycleAdapter.isOpenSelectMode(true);
                }
            }
        });

        delectTv = (TextView) findViewById(R.id.delect);
        delectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycleAdapter.delectPhoto();
            }
        });
        initRecycle();
    }

    private void initRecycle(){
        getAdaterDates();
        recycleAdapter = new PhotoAdapter(datas);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (recycleAdapter.getItemViewType(position) == PhotoDataBean.TYPE_TITLE){
                    return 4;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recycleAdapter);
    }

    /**
     * 获取图片文件
     * 装载进 ArrayList<PhotoDataBean> datas
     */
    private void getAdaterDates(){
        String photoFilePath = PathUtil.getPhotoPath();
        File dateFile = new File(photoFilePath);
        if (!dateFile.exists()){
            if (!dateFile.mkdirs()){
                return;
            }
        }

        File[] dateFiles = dateFile.listFiles();

        //日期文件夹
        List<File> dateFilesList = new ArrayList<>();

        //获取photoFiles中的所有文件夹
        for (File file : dateFiles){
            if (file.isDirectory()){
                dateFilesList.add(file);
            }
        }

        //根据文件创建日期排序
        getReverseFile(dateFilesList);

        //获取的标题、图片数据
        datas = new ArrayList<>();

        //获取各个文件夹目录下的图片
        for (int i = 0; i < dateFilesList.size(); i++) {
            PhotoDataBean titleBean = new PhotoDataBean(dateFilesList.get(i).getName(), PhotoDataBean.TYPE_TITLE);
            datas.add(titleBean);
            File[] photoFiles = dateFilesList.get(i).listFiles();
            for (int j = 0; j < photoFiles.length; j++) {
                String fileName = photoFiles[j].getName();
                if (fileName.endsWith(".jpg") | fileName.endsWith(".png")){
                    PhotoDataBean photoBean = new PhotoDataBean(dateFilesList.get(i).getPath()+"/"+fileName, PhotoDataBean.TYPE_PHOTO);
                    datas.add(photoBean);
                }
            }
        }
    }

    /**
     * 根据日期进行降序排列
     * @param files
     */
    public void getReverseFile(List<File> files) {
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File t0, File t1) {
                return t0.lastModified() > t1.lastModified() ? -1 : 0;
            }
        });
    }

}
