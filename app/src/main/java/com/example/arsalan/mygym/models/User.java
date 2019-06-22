package com.example.arsalan.mygym.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class User implements Parcelable {
    /*        "UserId": "24",
        "UserName": "9179154862",
        "Password": "31660",
        "Name": "",
        "Family": "",
        "Phone": "",
        "Email": "",
        "Mobile": "9179154862",
        "Age": "0",
        "Address": "",
        "PictureUrl": "",
        "PictureUrlArray": null,
        "ThumbUrl": null,
        "ThumbUrlArray": null,
        "BirthDateFa": "",
        "Gender": "True",
        "Weight": "0",
        "CityId": "0",
        "Intro": "",
        "roleId": "0",
        "RoleName": null,
        "RegisterDate": "5/7/2018 9:56:52 AM",
        "BirthDate": "",
        "RegisterDateFa": "1397/02/17",
        "RegisterTime": "09:56:52",
        "BirthTime": "",
        "IsConfirmed": "True"*/
    @PrimaryKey
    @SerializedName("UserId")
    long id;
    private String password;
    @SerializedName("UserName")
    private String userName;
    @SerializedName("Name")
    private String name;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("Age")
    private String age;
    @SerializedName("Address")
    private String address;
    @SerializedName("Weight")
    private String weight;
    @SerializedName("CityId")
    private long cityId;
    @SerializedName("PictureUrl")
    private String PictureUrl;
    @SerializedName("ThumbUrl")
    private String ThumbUrl;
    @SerializedName("Gender")
    private boolean gender;
    @SerializedName("RoleId")
    private int roleId;
    @SerializedName("RoleName")
    private String roleName;
    @SerializedName("IsConfirmed")
    private boolean isConfirmed;
    @SerializedName("TrainerUserId")
    private long trainerId;
    @SerializedName("RegisterDateFa")
    private String registerDate;
    @SerializedName("BirthDateFa")
    private String birthdayDateFa;


    public User() {
    }

    protected User(Parcel in) {
        id = in.readLong();
        password = in.readString();
        userName = in.readString();
        name = in.readString();
        phone = in.readString();
        age = in.readString();
        address = in.readString();
        weight = in.readString();
        cityId = in.readLong();
        PictureUrl = in.readString();
        ThumbUrl = in.readString();
        gender = in.readByte() != 0;
        roleId = in.readInt();
        roleName = in.readString();
        isConfirmed = in.readByte() != 0;
        trainerId = in.readLong();
        registerDate = in.readString();
        birthdayDateFa = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(password);
        dest.writeString(userName);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(age);
        dest.writeString(address);
        dest.writeString(weight);
        dest.writeLong(cityId);
        dest.writeString(PictureUrl);
        dest.writeString(ThumbUrl);
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeInt(roleId);
        dest.writeString(roleName);
        dest.writeByte((byte) (isConfirmed ? 1 : 0));
        dest.writeLong(trainerId);
        dest.writeString(registerDate);
        dest.writeString(birthdayDateFa);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isMale() {
        return gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    public String getThumbUrl() {
        return ThumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        ThumbUrl = thumbUrl;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(long trainerId) {
        this.trainerId = trainerId;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getBirthdayDateFa() {
        return birthdayDateFa;
    }

    public void setBirthdayDateFa(String birthdayDateFa) {
        this.birthdayDateFa = birthdayDateFa;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                ", weight='" + weight + '\'' +
                ", cityId=" + cityId +
                ", PictureUrl='" + PictureUrl + '\'' +
                ", ThumbUrl='" + ThumbUrl + '\'' +
                ", gender=" + gender +
                ", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", isConfirmed=" + isConfirmed +
                ", trainerId=" + trainerId +
                ", registerDate='" + registerDate + '\'' +
                ", birthdayDateFa='" + birthdayDateFa + '\'' +
                '}';
    }
}
