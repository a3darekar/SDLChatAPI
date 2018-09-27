package sdl_apps.sdlchatapi.models;

import com.google.gson.annotations.SerializedName;

public class Leaves {

    @SerializedName("id")
    int Pk;
    @SerializedName("employee")
    int employee;
    @SerializedName("count")
    int count;
    @SerializedName("leavetype")
    String type;

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public int getPk() {
        return Pk;
    }

    public void setPk(int pk) {
        Pk = pk;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Leaves{" +
                "count=" + count +
                ", type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}