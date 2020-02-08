package it.guaraldi.to_dotaskmanager.notification;

public class MockNotificationData {

    // Standard notification values:
    protected String mContentTitle;
    protected String mContentText;
    protected int mPriority;

    // Notification channel values:
    protected String mChannelId;


    // Notification Standard notification get methods:
    public String getContentTitle() {
        return mContentTitle;
    }

    public String getContentText() {
        return mContentText;
    }

    public int getPriority() {
        return mPriority;
    }

    // Channel values (O and above) get methods:
    public String getChannelId() {
        return mChannelId;
    }

}
