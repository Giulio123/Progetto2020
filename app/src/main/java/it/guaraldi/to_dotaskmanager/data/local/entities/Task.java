package it.guaraldi.to_dotaskmanager.data.local.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

/**
 * Created by sugfdo on 09/06/19.
 */

@Entity(tableName = "task", primaryKeys = {"id","email"})
public class Task {

    @NonNull
    @ColumnInfo(name = "id")
    private final String mId;

    @NonNull
    @ColumnInfo(name = "title")
    private final String title;

    @NonNull
    @ColumnInfo(name = "email")
    private final String email;

    @NonNull
    @ColumnInfo(name = "all_day")
    private final boolean allDay;

    @NonNull
    @ColumnInfo(name = "group_id")
    private final String groupId;

    @NonNull
    @ColumnInfo(name = "priority")
    private final int priority;

    @NonNull
    @ColumnInfo(name = "category")
    private final String category;

    @NonNull
    @ColumnInfo(name = "status")
    private final String status;

    @ColumnInfo(name = "start")
    private final String start;

    @ColumnInfo(name = "end")
    private final String end;

    @ColumnInfo(name = "longitude")
    private final String longitude;

    @ColumnInfo(name = "latitude")
    private final String latitude;

    @ColumnInfo(name = "description")
    private final String description;

    @NonNull
    @ColumnInfo(name = "color")
    private final String color;

    public Task(@NonNull String mId, @NonNull String title, @NonNull String email, @NonNull boolean allDay,
                @NonNull String groupId, @NonNull int priority, @NonNull String category, @NonNull String status,
                String start, String end, String longitude, String latitude, String description, String color) {
        this.mId = mId;
        this.title = title;
        this.email = email;
        this.allDay = allDay;
        this.groupId = groupId;
        this.priority = priority;
        this.category = category;
        this.status = status;
        this.start = start;
        this.end = end;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.color = color;
    }

    @Ignore
    public Task(@NonNull String id ,@NonNull String groupId, @NonNull String title, @NonNull String email, @NonNull boolean allDay,
                @NonNull int priority, @NonNull String category,
                String start, String end, String description, String color){
        this.mId = id;
        this.title = title;
        this.email = email;
        this.allDay = allDay;
        this.groupId = groupId;
        this.priority = priority;
        this.category = category;
        this.status = "pending";
        this.start = start;
        this.end = end;
        this.longitude = "0.0";
        this.latitude = "0.0";
        this.description = description;
        this.color = color;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public boolean isAllDay() {
        return allDay;
    }

    @NonNull
    public String getGroupId() {
        return groupId;
    }

    @NonNull
    public int getPriority() {
        return priority;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    public String getColor() {
        return color;
    }

    @Ignore
    @Override
    public String toString() {
        return "Task{" +
                "mId='" + mId + '\'' +
                ", title='" + title + '\'' +
                ", email='" + email + '\'' +
                ", allDay=" + allDay +
                ", groupId='" + groupId + '\'' +
                ", priority=" + priority +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
