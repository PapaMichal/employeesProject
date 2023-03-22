package com.example.employeesassignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

public class Employee implements Parcelable {
    private String name;
    private String address;
    private String education;
    private ArrayList<String> fieldsOfExpertise;
    private int employmentRate;
    private int hourlyPay;
    private final Bitmap picture;
    public Employee(String name, String address, String education, ArrayList<String> fieldsOfExpertise, int employmentRate, int hourlyPay, Bitmap picture) {
        this.name = name;
        this.address = address;
        this.education = education;
        this.fieldsOfExpertise = fieldsOfExpertise;
        this.employmentRate = employmentRate;
        this.hourlyPay = hourlyPay;
        this.picture = picture;
    }

    protected Employee(Parcel in) {
        name = in.readString();
        address = in.readString();
        education = in.readString();
        fieldsOfExpertise = in.createStringArrayList();
        employmentRate = in.readInt();
        hourlyPay = in.readInt();
        byte[] byteArray = in.createByteArray();
        picture = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(hourlyPay);
        parcel.writeInt(employmentRate);
        parcel.writeString(education);
        parcel.writeString(address);
        parcel.writeString(name);
        parcel.writeString(name);
        parcel.writeStringList(fieldsOfExpertise);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        parcel.writeByteArray(byteArray);
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setEducation(String education) {
        this.education = education;
    }
    public void setFieldsOfExpertise(ArrayList<String> fieldsOfExpertise) {
        this.fieldsOfExpertise = fieldsOfExpertise;
    }
    public void setEmploymentRate(int employmentRate) {
        this.employmentRate = employmentRate;
    }
    public void setHourlyPay(int hourlyPay) {
        this.hourlyPay = hourlyPay;
    }


    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getEducation() {
        return education;
    }
    public ArrayList<String> getFieldsOfExpertise() {
        return fieldsOfExpertise;
    }
    public String getFieldsOfExpertiseAsString() {
        StringBuilder fieldsStr = new StringBuilder();
        if (fieldsOfExpertise.size() > 0) {
            fieldsStr.append(fieldsOfExpertise.get(0));
            for (int i = 1; i < fieldsOfExpertise.size(); i++) {
                fieldsStr.append(", ").append(fieldsOfExpertise.get(i));
            }
        }
        return fieldsStr.toString();
    }

    public int getEmploymentRate() {
        return employmentRate;
    }
    public int getHourlyPay() {
        return hourlyPay;
    }
    public Bitmap getPicture() {return picture;}

    public static Comparator<Employee> nameAsc = (employee1, employee2) -> {
        String name1 = employee1.getName().toLowerCase();
        String name2 = employee2.getName().toLowerCase();
        return name1.compareTo(name2);
    };

    public static Comparator<Employee> hourlyPayDsc = (employee1, employee2) -> {
        int hourlyPay1 = employee1.getHourlyPay();
        int hourlyPay2 = employee2.getHourlyPay();
        return Integer.compare(hourlyPay2,  hourlyPay1);
    };

    public static Comparator<Employee> employmentRateDsc = (employee1, employee2) -> {
        int employmentRate1 = employee1.getEmploymentRate();
        int employmentRate2 = employee2.getEmploymentRate();
        return Integer.compare(employmentRate2,  employmentRate1);
    };


    @Override
    public int describeContents() {
        return 0;
    }
}
