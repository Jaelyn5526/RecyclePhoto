package jaelyn.blg;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

/**
 * Created by jaelyn on 17-02-20.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder>{

    public ArrayList<PhotoDataBean> datas = new ArrayList<>();
    private boolean isSelectMode = false;


    public PhotoAdapter(ArrayList<PhotoDataBean> datas){
        this.datas = (ArrayList<PhotoDataBean>) datas.clone();
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoHolder photoHolder;
        View itemView;
        if (viewType == PhotoDataBean.TYPE_TITLE){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item_title, null, false);
            photoHolder = new PhotoHolder(itemView, PhotoDataBean.TYPE_TITLE);
        }else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_recycler_item, null, false);
            photoHolder = new PhotoHolder(itemView, PhotoDataBean.TYPE_PHOTO);
        }
        return photoHolder;
    }


    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        if (holder.type == PhotoDataBean.TYPE_TITLE){
            holder.titleTV.setText(datas.get(position).pathOrdate);
        }else {
            displayImg(datas.get(position).pathOrdate, holder.imgView);
            if (isSelectMode) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(datas.get(position).isSelect);
            } else {
                holder.checkBox.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }


    @Override
    public int getItemViewType(int position) {
        return datas.get(position).type;
    }


    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        int type;
        TextView titleTV;
        ImageView imgView;
        CheckBox checkBox;

        public PhotoHolder(View itemView) {
            super(itemView);
        }

        public PhotoHolder(View itemView, int type){
            super(itemView);
            itemView.setOnClickListener(this);
            this.type = type;
            if (type == PhotoDataBean.TYPE_TITLE){
                titleTV = (TextView) itemView.findViewById(R.id.title);
            }else {
                imgView = (ImageView) itemView.findViewById(R.id.image_view);
                checkBox = (CheckBox) itemView.findViewById(R.id.toggle_button);
            }
        }

        @Override
        public void onClick(View view) {
            if (type == 0) {
                if (isSelectMode) {
                    checkBox.setChecked(!checkBox.isChecked());
                    changeDatasSelect(getPosition(), checkBox.isChecked());
                } else {

                }
            }
        }
    }

    /**
     * 使用Glide框架加载图片
     * @param url
     * @param imageView
     */
    private static void displayImg(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    /**
     * 开启、关闭选择模式
     * @param isOpen
     */
    public void isOpenSelectMode(boolean isOpen){
        isSelectMode = isOpen;
        notifyDataSetChanged();
    }

    /**
     * 修改数据状态 ture-选中状态  false-未选中状态
     * @param position
     * @param isSelect
     */
    private void changeDatasSelect(int position, boolean isSelect) {
        datas.get(position).isSelect = isSelect;
        int titleIndex = -1;
        boolean isUnSelectFile = true;
        for (int i = position-1; i >= 0; i--) {
            PhotoDataBean bean = datas.get(i);
            if (bean.type == 1) {
                titleIndex = i;
                break;
            } else if (bean.isSelect != isSelect) {
                isUnSelectFile = false;
            }
        }
        if (titleIndex != -1) {
            if (isUnSelectFile){
                for (int i = position+1; i < datas.size() && datas.get(i).type == 0; i++) {
                    PhotoDataBean bean = datas.get(i);
                    if (bean.isSelect != isSelect) {
                        isUnSelectFile = false;
                        break;
                    }
                }
            }
            if (isUnSelectFile) {
                datas.get(titleIndex).isSelect = isSelect;
            }
        }
    }


    /**
     * 删除图片以及空的文件夹
     */
    public void delectPhoto() {
        ArrayList<PhotoDataBean> currentPhotoDatas = (ArrayList<PhotoDataBean>) datas.clone();
        ArrayList<String> currentPhotos = (ArrayList<String>) datas.clone();
        for (int i = 0; i < currentPhotoDatas.size(); i++) {
            PhotoDataBean bean = currentPhotoDatas.get(i);
            if (!bean.isSelect) {
                continue;
            }
            if (bean.type == 1){
                FileUtils.deleteDirectory(PathUtil.getPhotoPath()+"/"+bean.pathOrdate);
            }else {
                FileUtils.deleteFile(bean.pathOrdate);
            }
            currentPhotoDatas.remove(bean);
            i--;
        }
        currentPhotos.clear();
        for (int i = 0; i < currentPhotoDatas.size(); i++) {
            if (currentPhotoDatas.get(i).type == 0){
                currentPhotos.add(currentPhotoDatas.get(i).pathOrdate);
            }
        }
        datas = (ArrayList<PhotoDataBean>) currentPhotoDatas.clone();
        notifyDataSetChanged();
    }

}
