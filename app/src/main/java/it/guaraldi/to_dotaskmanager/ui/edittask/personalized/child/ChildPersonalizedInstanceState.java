package it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class ChildPersonalizedInstanceState implements Parcelable {

    private boolean neverBtn;
    private boolean theDayBtn;
    private boolean afterBtn;
    private String endDayTv;
    private String numberOccorenceEdit;
    private String monthSpinner;
    private int monthSpinnerId;
    private boolean [] daysSelection;

    public ChildPersonalizedInstanceState(boolean neverBtn, boolean theDayBtn, boolean afterBtn, String endDayTv, String numberOccorenceEdit, String monthSpinner, int monthSpinnerId, boolean[] daysSelection) {
        this.neverBtn = neverBtn;
        this.theDayBtn = theDayBtn;
        this.afterBtn = afterBtn;
        this.endDayTv = endDayTv;
        this.numberOccorenceEdit = numberOccorenceEdit;
        this.monthSpinner = monthSpinner;
        this.monthSpinnerId = monthSpinnerId;
        this.daysSelection = daysSelection;
    }

    protected ChildPersonalizedInstanceState(Parcel in) {
        neverBtn = in.readByte() != 0;
        theDayBtn = in.readByte() != 0;
        afterBtn = in.readByte() != 0;
        endDayTv = in.readString();
        numberOccorenceEdit = in.readString();
        monthSpinner = in.readString();
        monthSpinnerId = in.readInt();
        daysSelection = in.createBooleanArray();
    }

    public static final Creator<ChildPersonalizedInstanceState> CREATOR = new Creator<ChildPersonalizedInstanceState>() {
        @Override
        public ChildPersonalizedInstanceState createFromParcel(Parcel in) {
            return new ChildPersonalizedInstanceState(in);
        }

        @Override
        public ChildPersonalizedInstanceState[] newArray(int size) {
            return new ChildPersonalizedInstanceState[size];
        }
    };

    public boolean isNeverBtn() {
        return neverBtn;
    }

    public boolean isTheDayBtn() {
        return theDayBtn;
    }

    public boolean isAfterBtn() {
        return afterBtn;
    }

    public String getEndDayTv() {
        return endDayTv;
    }

    public String getNumberOccorenceEdit() {
        return numberOccorenceEdit;
    }

    public String getMonthSpinner() {
        return monthSpinner;
    }

    public boolean[] getDaysSelection() {
        return daysSelection;
    }

    public int getMonthSpinnerId() {
        return monthSpinnerId;
    }

    public boolean allDayUnchecked(){
        for(boolean r : daysSelection)
            if(r) return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(neverBtn ? 1 : 0);
        dest.writeInt(theDayBtn ? 1 : 0);
        dest.writeInt(afterBtn ? 1 : 0);
        dest.writeString(endDayTv);
        dest.writeString(numberOccorenceEdit);
        dest.writeString(monthSpinner);
        dest.writeInt(monthSpinnerId);
        dest.writeBooleanArray(daysSelection);
    }

    @Override
    public String toString() {
        return "ChildPersonalizedInstanceState{" +
                "neverBtn=" + neverBtn +
                ", theDayBtn=" + theDayBtn +
                ", afterBtn=" + afterBtn +
                ", endDayTv='" + endDayTv + '\'' +
                ", numberOccorenceEdit='" + numberOccorenceEdit + '\'' +
                ", monthSpinner='" + monthSpinner + '\'' +
                ", monthSpinnerId=" + monthSpinnerId +
                ", daysSelection=" + Arrays.toString(daysSelection) +
                '}';
    }
}
