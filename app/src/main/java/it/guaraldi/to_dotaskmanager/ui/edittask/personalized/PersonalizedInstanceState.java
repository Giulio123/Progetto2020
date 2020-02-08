package it.guaraldi.to_dotaskmanager.ui.edittask.personalized;

import android.os.Parcel;
import android.os.Parcelable;

import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child.ChildPersonalizedInstanceState;

public class PersonalizedInstanceState implements Parcelable {

    private String date;
    private int periodSelectedPosition;
    private String periodNameSelection;
    private int numberPeriodRepetition;


    public PersonalizedInstanceState(String date, int periodSelectedPosition, String periodNameSelection, int numberPeriodRepetition) {
        this.date = date;
        this.periodSelectedPosition = periodSelectedPosition;
        this.periodNameSelection = periodNameSelection;
        this.numberPeriodRepetition = numberPeriodRepetition;

    }

    protected PersonalizedInstanceState(Parcel in) {
        date = in.readString();
        periodSelectedPosition = in.readInt();
        periodNameSelection = in.readString();
        numberPeriodRepetition = in.readInt();

    }

    public static final Creator<PersonalizedInstanceState> CREATOR = new Creator<PersonalizedInstanceState>() {
        @Override
        public PersonalizedInstanceState createFromParcel(Parcel in) {
            return new PersonalizedInstanceState(in);
        }

        @Override
        public PersonalizedInstanceState[] newArray(int size) {
            return new PersonalizedInstanceState[size];
        }
    };

    public String getDate() {
        return date;
    }

    public int getPeriodSelectedPosition() {
        return periodSelectedPosition;
    }

    public String getPeriodNameSelection() {
        return periodNameSelection;
    }

    public int getNumberPeriodRepetition() {
        return numberPeriodRepetition;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPeriodSelectedPosition(int periodSelectedPosition) {
        this.periodSelectedPosition = periodSelectedPosition;
    }

    public void setPeriodNameSelection(String periodNameSelection) {
        this.periodNameSelection = periodNameSelection;
    }

    public void setNumberPeriodRepetition(int numberPeriodRepetition) {
        this.numberPeriodRepetition = numberPeriodRepetition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getDate());
        dest.writeInt(getPeriodSelectedPosition());
        dest.writeString(getPeriodNameSelection());
        dest.writeInt(getNumberPeriodRepetition());

    }

    @Override
    public String toString() {
        return "PersonalizedInstanceState{" +
                "date='" + date + '\'' +
                ", periodSelectedPosition=" + periodSelectedPosition +
                ", periodNameSelection='" + periodNameSelection + '\'' +
                ", numberPeriodRepetition='" + numberPeriodRepetition + '\'' +
                '}';
    }
}
