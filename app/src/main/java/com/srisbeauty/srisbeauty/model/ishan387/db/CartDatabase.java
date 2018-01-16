package com.srisbeauty.srisbeauty.model.ishan387.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.srisbeauty.srisbeauty.model.CartItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 12-11-2017.
 */

public class CartDatabase extends SQLiteAssetHelper {


    private static final  String DB_NAME="Sbcartdb.db";
    private static final int DB_VER= 2;
    public CartDatabase(Context context) {
        super(context, DB_NAME,null,DB_VER);
    }



    public List<CartItems> getCart(){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"productId", "productName","price","serviceTime"};
        String sqlTable = "SBCART";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);
        final List<CartItems> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new CartItems(c.getString(c.getColumnIndex("productid")),
                                         c.getString(c.getColumnIndex("productname")),
                                         c.getString(c.getColumnIndex("price")),
                                         c.getString(c.getColumnIndex("servicetime"))));

            }while (c.moveToNext());
        }

        return result;
    }


    public void addToCart(CartItems item)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO SBCART(productid,productname,price,servicetime) VALUES('%s','%s','%s','%s')",

        item.getProductId(), item.getProductName(),item.getPrice(),item.getServiceTime());
        db.execSQL(query);

    }

    public void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM SBCART");
        db.execSQL(query);
    }

    public int countOfItem(String itemName)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM SBCART WHERE productName= '"+ itemName+"'");
        Cursor c = db.rawQuery(query, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public void deleteItem(String itemName)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM SBCART WHERE productName = '"+ itemName+"'");
        db.execSQL(query);
    }
}
