package charlesbest.com.greenmarket;

/**
 * Created by BEN on 14/06/2018.
 */

public class UserModel {
    String name;
    String email;
    String phone;
    String account_type;

    public UserModel() {
    }

    public UserModel(String name, String email, String phone, String account_type) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.account_type = account_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }
}
