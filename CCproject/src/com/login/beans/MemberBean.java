package com.login.beans;

public class MemberBean {
	 // ���̵�, ��й�ȣ, �ּ�, ��ȭ�� ���� ������Ƽ(�ɹ�����)
    // ������Ƽ�� ���� ������ �� ���� private�� ����Ѵ�.
    private String id;
    private String pw;
    private String name;
    private String gender;
    private String email;
    
    /* �����͸� �������ų�(get), �����ϴ�(set)
    *  ����� �ϴ� �޼��带 �����.
    *  - �����͸� �������� ��� - get�޼���
    *  - �����͸� �����ϴ� ��� - set�޼���
    */
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public String getName() {
    	return name;
    }
    public void setName(String name) {
    	this.name = name;
    }
    public String getGender() {
    	return gender;
    }
    public void setGender(String gender) {
    	this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
   
}
