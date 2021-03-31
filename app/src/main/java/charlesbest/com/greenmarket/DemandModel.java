package charlesbest.com.greenmarket;

/**
 * Created by BEN on 14/06/2018.
 */

public class DemandModel {
    String name;
    String location;
    String number;
    String key;
    String seller;
    String description;

    public DemandModel() {
    }

    public DemandModel(String name,  String location, String number, String key, String seller, String description) {
        this.name = name;
        this.location = location;
        this.number = number;
        this.key = key;
        this.seller = seller;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
