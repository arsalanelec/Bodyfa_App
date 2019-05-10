package com.example.arsalan.mygym.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

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
    private String isConfirmed;

    @SerializedName("TrainerUserId")
    private long trainerId;

    @SerializedName("RegisterDateFa")
    private String registerDate;

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
        isConfirmed = in.readString();
        trainerId = in.readLong();
        registerDate = in.readString();
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
        return isConfirmed.equals("True");
    }

    public String getRoleName() {
        return roleName;
    }

    public boolean isMale() {
        return gender;
    }

    public long getId() {
        return id;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public String getThumbUrl() {
        return ThumbUrl;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getWeight() {
        return weight;
    }

    public long getCityId() {
        return cityId;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        ThumbUrl = thumbUrl;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setIsConfirmed(String isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }


    public boolean isGender() {
        return gender;
    }

    public String getIsConfirmed() {
        return isConfirmed;
    }

    public long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(long trainerId) {
        this.trainerId = trainerId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(password);
        parcel.writeString(userName);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(age);
        parcel.writeString(address);
        parcel.writeString(weight);
        parcel.writeLong(cityId);
        parcel.writeString(PictureUrl);
        parcel.writeString(ThumbUrl);
        parcel.writeByte((byte) (gender ? 1 : 0));
        parcel.writeInt(roleId);
        parcel.writeString(roleName);
        parcel.writeString(isConfirmed);
        parcel.writeLong(trainerId);
        parcel.writeString(registerDate);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
