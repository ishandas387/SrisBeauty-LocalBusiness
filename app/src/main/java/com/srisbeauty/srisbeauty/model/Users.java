package com.srisbeauty.srisbeauty.model;

/**
 * Created by ishan on 18-12-2017.
 */

public class Users  {

    int _id;
    String _name,_mail_id,_phone_number,_address_at,_address_near,_address_city;

    public Users( String _name, String _mail_id, String _phone_number, String _address_at, String _address_near, String _address_city) {

        this._name = _name;
        this._mail_id = _mail_id;
        this._phone_number = _phone_number;
        this._address_at = _address_at;
        this._address_near = _address_near;
        this._address_city = _address_city;
    }

    public Users(){
    }

    public Users(int id,String a, String b, String c, String d,String e, String f, String g) {
        this._id=id;
        this._name=a;
        this._phone_number=b;
        this._mail_id=c;
        this._address_at=d;
        this._address_near=e;
        this._address_city=f;

    }

    public Users(String a, String b, String c, String d,String e, String f, String g) {
        this._name=a;
        this._phone_number=b;
        this._mail_id=c;
        this._address_at=d;
        this._address_near=e;
        this._address_city=f;

    }

    public void setNm(String a)
    {
        this._name=a;
    }
    public String getNm()
    {
        return this._name;
    }

    public void setMail(String a)
    {
        this._mail_id=a;
    }
    public String getMail()
    {
        return this._mail_id;
    }

    public void setNu(String a)
    {
        this._phone_number=a;
    }
    public String getNu()
    {
        return this._phone_number;
    }

    public void setAddAt(String a)
    {
        this._address_at=a;
    }
    public String getAddAt()
    {
        return this._address_at;
    }


    public void setAddNear(String a)
    {
        this._address_near=a;
    }
    public String getAddNear()
    {
        return this._address_near;
    }

    public void setAddCity(String a)
    {
        this._address_city=a;
    }
    public String getAddCity()
    {
        return this._address_city;
    }


    public void setID(int a)
    {
        this._id=a;
    }
    public int getID()
    {
        return this._id;
    }

}
