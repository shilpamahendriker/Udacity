package com.example.avni.a20181018_shilpamahendriker_nycschools;

// School Class
public class School {
    private String bdn;
    private String schoolName;
    private String schoolBorough;
    private String schoolLocation;

    //defining getter methods
    public String getBdn() {
        return bdn;
    }
    public String getSchoolName() {
        return schoolName;
    }
    public String getSchoolBorough() {
        return schoolBorough;
    }
    public String getSchoolLocation() {
        return schoolLocation;
    }


    // Defining Constructor
    public School(String bdn, String schoolName, String schoolBorough, String schoolLocation) {
        this.bdn = bdn;
        this.schoolName = schoolName;
        this.schoolBorough = schoolBorough;
        this.schoolLocation = schoolLocation;
    }
}
