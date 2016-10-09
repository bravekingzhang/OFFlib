package test.tencent.com.offlib.vo;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

import test.tencent.com.offlib.util.Validation;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by hoollyzhang on 16/9/27.
 * Description :这里只是一个demo vo
 */

@Entity
public class Post implements Parcelable, Validation, Serializable {


    @Transient
    public static final long serialVersionUID = 1;

    @Id(autoincrement = true)
    private Long id;

    private boolean mPending;//发送状态

    private String mLocalUniqId;//本地唯一Id,用于给服务器端区分是否重复发送

    private String mood;

    private long ctime;

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public boolean ismPending() {
        return mPending;
    }

    public void setmPending(boolean mPending) {
        this.mPending = mPending;
    }

    public String getmLocalUniqId() {
        return mLocalUniqId;
    }

    public void setmLocalUniqId(String mLocalUniqId) {
        this.mLocalUniqId = mLocalUniqId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean validate() {
        return mood != null && !mood.equals("");
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeByte(this.mPending ? (byte) 1 : (byte) 0);
        dest.writeString(this.mLocalUniqId);
        dest.writeString(this.mood);
        dest.writeLong(this.ctime);
    }

    public boolean getMPending() {
        return this.mPending;
    }

    public void setMPending(boolean mPending) {
        this.mPending = mPending;
    }

    public String getMLocalUniqId() {
        return this.mLocalUniqId;
    }

    public void setMLocalUniqId(String mLocalUniqId) {
        this.mLocalUniqId = mLocalUniqId;
    }

    public Post() {
    }

    protected Post(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mPending = in.readByte() != 0;
        this.mLocalUniqId = in.readString();
        this.mood = in.readString();
        this.ctime = in.readLong();
    }

    @Generated(hash = 1899428625)
    public Post(Long id, boolean mPending, String mLocalUniqId, String mood,
            long ctime) {
        this.id = id;
        this.mPending = mPending;
        this.mLocalUniqId = mLocalUniqId;
        this.mood = mood;
        this.ctime = ctime;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
