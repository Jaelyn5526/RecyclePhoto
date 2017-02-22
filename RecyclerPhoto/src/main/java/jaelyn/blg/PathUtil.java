package jaelyn.blg;

import android.os.Environment;

import java.io.File;

/**
 * Created by zaric on 17-02-20.
 */

public class PathUtil {

    // 主目录名
    private static final String HOME_PATH_NAME = "yaya";
    // 照片和视频的子目录名
    private static final String PHOTO_PATH_NAME = "picture";
    private static final String VIDEO_PATH_NAME = "video";

    /**
     * 获取应用数据主目录
     *
     * @return 主目录路径
     */
    static public String getHomePath(){
        String homePath = null;

        try {
            String extStoragePath = Environment.getExternalStorageDirectory().getCanonicalPath();
            homePath = new File(extStoragePath, HOME_PATH_NAME).getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return homePath;
    }

    /**
     * 获取主目录下子目录
     *
     * @param dir 子目录名称
     * @return 子目录路径
     */
    static public String getSubDir(String dir) {
        // 获取主目录路径
        String homePath = getHomePath();

        if (homePath == null) {
            return null;
        }

        String subDirPath = null;

        try {
            // 获取展开的子目录路径
            subDirPath = new File(homePath, dir).getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subDirPath;
    }

    /**
     * 获取主目录下照片目录
     *
     * @return 照片目录路径
     */
    static public String getPhotoPath() {
        return getSubDir(PHOTO_PATH_NAME);
    }

    /**
     * 获取主目录下视频目录
     *
     * @return 视频目录路径
     */
    static public String getVideoPath() {
        return getSubDir(VIDEO_PATH_NAME);
    }

}
