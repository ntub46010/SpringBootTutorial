package com.example.demo.param;

import java.time.LocalDate;

public class StudentRequestParam {
    // sort and pagination
    private String sortField;
    private String sortDirection;
    private Integer page;
    private Integer size;

    // customized parameters
    private String email;
    private String phone;
    private Integer gradeFrom;
    private Integer gradeTo;
    private LocalDate birthdayFrom;
    private LocalDate birthdayTo;
    private String certType;
    private Integer certScoreFrom;
    private Integer certScoreTo;

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

    public Integer getGradeFrom() {
        return gradeFrom;
    }

    public void setGradeFrom(Integer gradeFrom) {
        this.gradeFrom = gradeFrom;
    }

    public Integer getGradeTo() {
        return gradeTo;
    }

    public void setGradeTo(Integer gradeTo) {
        this.gradeTo = gradeTo;
    }

    public LocalDate getBirthdayFrom() {
        return birthdayFrom;
    }

    public void setBirthdayFrom(LocalDate birthdayFrom) {
        this.birthdayFrom = birthdayFrom;
    }

    public LocalDate getBirthdayTo() {
        return birthdayTo;
    }

    public void setBirthdayTo(LocalDate birthdayTo) {
        this.birthdayTo = birthdayTo;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public Integer getCertScoreFrom() {
        return certScoreFrom;
    }

    public void setCertScoreFrom(Integer certScoreFrom) {
        this.certScoreFrom = certScoreFrom;
    }

    public Integer getCertScoreTo() {
        return certScoreTo;
    }

    public void setCertScoreTo(Integer certScoreTo) {
        this.certScoreTo = certScoreTo;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}