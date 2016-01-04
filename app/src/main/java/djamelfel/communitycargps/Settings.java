package djamelfel.communitycargps;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by djamel on 04/01/16.
 */
public class Settings implements Parcelable{
    private int distance;
    private int timeLimite;

    Settings() {
        distance = 80;
        timeLimite = 60;
    }

    Settings(Parcel in) {
        readFromParcel(in);
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setTimeLimite(int timeLimite) {
        this.timeLimite = timeLimite;
    }

    public int getDistance() {
        return distance;
    }

    public int getTimeLimite() {
        return timeLimite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.distance);
        parcel.writeInt(this.timeLimite);
    }

    private void readFromParcel(Parcel in) {
        distance = in.readInt();
        timeLimite = in.readInt();
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel in) {
            return new Settings(in);
        }

        @Override
        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };

}
