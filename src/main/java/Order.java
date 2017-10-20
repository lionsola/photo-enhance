public class Order {
    private int filterID;
    private String picName;

    public Order() {}

    public Order(int filterID, String picName) {
        this.filterID = filterID;
        this.picName = picName;
    }

    public int getFilterID() {
        return filterID;
    }

    public String getPicName() {
        return picName;
    }

    public void setFilterID(int filterID) {
        this.filterID = filterID;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }
}
