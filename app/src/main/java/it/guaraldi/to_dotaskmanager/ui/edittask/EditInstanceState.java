package it.guaraldi.to_dotaskmanager.ui.edittask;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

public class EditInstanceState implements Parcelable {

    private static final String TAG = "EditInstanceState";
    private String mTitle;
    private String mEmail;
    private boolean mAllDay;
    private String mStartD;
    private String mEndD;
    private String mStartH;
    private String mEndH;
    private String mPeriod;
    private int mPeriodPosition;
    private String mCategory;
    private int mCategoryPosition;
    private int mColor;
    private int mPriority;
    private String mDescription;
    private boolean errorDate;

    public EditInstanceState(String mTitle, String mEmail, boolean mAllDay, String mStartD, String mEndD, String mStartH, String mEndH, String mPeriod, int mPeriodPosition, String mCategory,int categoryPosition, int mColor, int mPriority, String mDescription, boolean errorDate) {
        this.mTitle = mTitle;
        this.mEmail = mEmail;
        this.mAllDay = mAllDay;
        this.mStartD = mStartD;
        this.mEndD = mEndD;
        this.mStartH = mStartH;
        this.mEndH = mEndH;
        this.mPeriod = mPeriod;
        this.mPeriodPosition = mPeriodPosition;
        this.mCategory = mCategory;
        this.mCategoryPosition = categoryPosition;
        this.mColor = mColor;
        this.mPriority = mPriority;
        this.mDescription = mDescription;
        this.errorDate = errorDate;
    }

    public int getmPeriodPosition() {
        return mPeriodPosition;
    }

    public boolean isErrorDate() {
        return errorDate;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmEmail() {
        return mEmail;
    }

    public boolean ismAllDay() {
        return mAllDay;
    }

    public String getmStartD() {
        return mStartD;
    }

    public String getmEndD() {
        return mEndD;
    }

    public String getmStartH() {
        return mStartH;
    }

    public String getmEndH() {
        return mEndH;
    }

    public String getmPeriod() {
        return mPeriod;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmCategory() {
        return mCategory;
    }

    public int getmCategoryPosition() {
        return mCategoryPosition;
    }

    public int getmColor() {
        return mColor;
    }

    public int getmPriority() {
        return mPriority;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mEmail);
        dest.writeInt(mAllDay ? 1 : 0);
        dest.writeString(mStartD);
        dest.writeString(mEndD);
        dest.writeString(mStartH);
        dest.writeString(mEndH);
        dest.writeInt(mPeriodPosition);
        dest.writeString(mPeriod);
        dest.writeString(mCategory);
        dest.writeInt(mCategoryPosition);
        dest.writeInt(mColor);
        dest.writeInt(mPriority);
        dest.writeString(mDescription);
        dest.writeInt(errorDate ? 1 : 0);
    }


    protected EditInstanceState(Parcel in) {
        mTitle = in.readString();
        mEmail = in.readString();
        mAllDay = in.readByte() != 0;
        mStartD = in.readString();
        mEndD = in.readString();
        mStartH = in.readString();
        mEndH = in.readString();
        mPeriodPosition = in.readInt();
        mPeriod = in.readString();
        mCategory = in.readString();
        mCategoryPosition = in.readInt();
        mColor = in.readInt();
        mPriority = in.readInt();
        mDescription = in.readString();
        errorDate = in.readByte() != 0;
    }

    public static final Creator<EditInstanceState> CREATOR = new Creator<EditInstanceState>() {
        @Override
        public EditInstanceState createFromParcel(Parcel in) {
            return new EditInstanceState(in);
        }

        @Override
        public EditInstanceState[] newArray(int size) {
            return new EditInstanceState[size];
        }
    };

    @Override
    public String toString() {
        return "EditInstanceState{" +
                "mTitle='" + mTitle + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mAllDay=" + mAllDay +
                ", mStartD='" + mStartD + '\'' +
                ", mEndD='" + mEndD + '\'' +
                ", mStartH='" + mStartH + '\'' +
                ", mEndH='" + mEndH + '\'' +
                ", mPeriod='" + mPeriod + '\'' +
                ", mPeriodPosition=" + mPeriodPosition +
                ", mCategory='" + mCategory + '\'' +
                ", mCategoryPosition=" + mCategoryPosition +
                ", mColor=" + mColor +
                ", mPriority=" + mPriority +
                ", mDescription='" + mDescription + '\'' +
                ", errorDate=" + errorDate +
                '}';
    }
}
