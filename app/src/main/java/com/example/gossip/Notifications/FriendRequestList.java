package com.example.gossip.Notifications;

public class FriendRequestList {
    String Name;
    String Mobile;
    String ProfilePic;
    boolean condition;
    public FriendRequestList(String Name, String profilePic, String Mobile,boolean cond){
        this.Name=Name;
        this.ProfilePic=profilePic;
        this.Mobile=Mobile;
        this.condition=cond;
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public void setName(String name) {
        Name = name;
    }
}
