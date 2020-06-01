package com.example.andy.myapplication;

class SubjectData {
    String SubjectName;
    String Link;
    String Image;

    public SubjectData(String subjectName, String link, String image) {
        this.SubjectName = subjectName;
        this.Link = link;
        this.Image = image;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
