package com.futurenav.cuong.futurenav.model;

import java.util.List;

/**
 * Created by Cuong on 8/25/2015.
 */
public class CodeOrg {

    private String description;
    private String generated;
    private String license;
    private List<School> schools;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        this.generated = generated;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }
}
