package com.srisbeauty.srisbeauty.model.ishan387.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.srisbeauty.srisbeauty.model.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 18-12-2017.
 */

public class UserDatabase extends SQLiteAssetHelper {

    private static final  String DB_NAME="Sbcartdb.db";
    private static final int DB_VER= 2;
    public UserDatabase(Context context) {
        super(context, DB_NAME,null,DB_VER);
    }


    public void addUserDetails(Users user)
    {
        cleanTable();
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO USERTABLE(KEY_UNAME,KEY_UPHNO,KEY_UMAIL,KEY_UADDAT,KEY_UADDNEAR,KEY_UADDCITY) VALUES('%s','%s','%s','%s','%s','%s')",

                user.getNm(), user.getNu(),user.getMail(),user.getAddAt(),user.getAddNear(),user.getAddCity());
        db.execSQL(query);

    }

    public void cleanTable()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM USERTABLE");
        db.execSQL(query);
    }


    public List<Users> getUser(){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"KEY_UNAME", "KEY_UPHNO","KEY_UMAIL","KEY_UADDAT","KEY_UADDNEAR","KEY_UADDCITY"};
        String sqlTable = "USERTABLE";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);
        final List<Users> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Users(c.getString(c.getColumnIndex("KEY_UNAME")),
                        c.getString(c.getColumnIndex("KEY_UPHNO")),
                        c.getString(c.getColumnIndex("KEY_UMAIL")),
                        c.getString(c.getColumnIndex("KEY_UADDAT")),
                        c.getString(c.getColumnIndex("KEY_UADDNEAR")),
                        c.getString(c.getColumnIndex("KEY_UADDCITY"))));

            }while (c.moveToNext());
        }

        return result;
    }

}
