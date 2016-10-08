package test.tencent.com.offdemo.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;
import test.tencent.com.offdemo.util.Validation;


/**
 * Created by hoollyzhang on 16/9/27.
 * Description :这里只是一个demo vo
 */

@RealmClass
public class Post implements RealmModel,Parcelable,Validation,Serializable {


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

    @Override
    public void validate() {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.mPending ? (byte) 1 : (byte) 0);
        dest.writeString(this.mLocalUniqId);
        dest.writeString(this.mood);
        dest.writeLong(this.ctime);
    }

    public Post() {
    }

    protected Post(Parcel in) {
        this.mPending = in.readByte() != 0;
        this.mLocalUniqId = in.readString();
        this.mood = in.readString();
        this.ctime = in.readLong();
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
