package helper;

public class Room {
    private String name;
    private int windownum;
    private int doornum;
    private int lightnum;
    private String nextroomname;

    public Room(String name, int windownum, int doornum, int lightnum, String nextroomname) {
        this.name = name;
        this.windownum = windownum;
        this.doornum = doornum;
        this.lightnum = lightnum;
        this.nextroomname = nextroomname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWindownum() {
        return windownum;
    }

    public void setWindownum(int windownum) {
        this.windownum = windownum;
    }

    public int getDoornum() {
        return doornum;
    }

    public void setDoornum(int doornum) {
        this.doornum = doornum;
    }

    public int getLightnum() {
        return lightnum;
    }

    public void setLightnum(int lightnum) {
        this.lightnum = lightnum;
    }

    public String getNextroomname() {
        return nextroomname;
    }

    public void setNextroomname(String nextroomname) {
        this.nextroomname = nextroomname;
    }

    @Override
    public String toString() {
        return "room{" +
                "name='" + name + '\'' +
                ", windownum=" + windownum +
                ", doornum=" + doornum +
                ", lightnum=" + lightnum +
                ", nextroomname='" + nextroomname + '\'' +
                '}';
    }
}
