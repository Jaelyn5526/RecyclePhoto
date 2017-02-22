package jaelyn.blg;

/**
 * Created by zaric on 16-12-28.
 */

public class PhotoDataBean {
    public static final int TYPE_PHOTO = 0;
    public static final int TYPE_TITLE = 1;

    public int type = 0; // 0-照片  1-日期
    public String pathOrdate;
    public boolean isSelect = false;

    public PhotoDataBean(String pathOrdate, int type) {
        this.pathOrdate = pathOrdate;
        this.type = type;
    }
}
