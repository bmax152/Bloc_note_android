package mb.solo.blocnote.orm;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mb.solo.blocnote.model.Categorie;

public class CategorieDao {
    private static final String TAG = "orm";
    private static Dao<Categorie, Integer> dao = null;

     public CategorieDao(Context context){
        if(dao == null){
            try {
                DbManager db = new DbManager(context);
                dao = db.getDao(Categorie.class);
            } catch (SQLException e) {
                Log.e(TAG, "CategorieDao - Error! ",e);
            }
        }
    }

    /**
     * CRUD
     */

    public List<Categorie> list(){

        List<Categorie> items = new ArrayList<>();
        try {
            items = dao.queryForAll();
            Log.i(TAG, "CategorieDao - SelectAll - OK");
        } catch (SQLException e) {
            Log.e(TAG, "CategorieDao - SelectAll - Error! ",e);
        }
        return items;
    }

    public Categorie find(int id){
        try {
            Log.i(TAG, "CategorieDao - Find OK");
            return dao.queryForId(id);
        } catch (SQLException e) {
            Log.e(TAG, "CategorieDao - Find-Error! ",e);
            return null;
        }
    }

    public void create(Categorie item){
        try {
            dao.create(item);
            Log.i(TAG, "CategorieDao - Create OK");
        } catch (SQLException e) {
            Log.e(TAG, "CategorieDao - Create - Error! ",e);
        }
    }

    public void update(Categorie item){
        try {
            dao.update(item);
            Log.i(TAG, "CategorieDao - Update OK");
        } catch (SQLException e) {
            Log.e(TAG, "CategorieDao - Update - Error! ",e);
        }
    }

    public void createOrUpdate(Categorie item){
        try {
            dao.createOrUpdate(item);
            Log.i(TAG, "CategorieDao - Create or Update OK");
        } catch (SQLException e) {
            Log.e(TAG, "CategorieDao - Create or Update - Error! ",e);
        }
    }

    public void delete(Categorie item){
        try {
            dao.delete(item);
            Log.i(TAG, "CategorieDao - Delete OK");
        } catch (SQLException e) {
            Log.e(TAG, "CategorieDao - Delete - Error! ",e);
        }
    }

    public void delete(int id) {
        Categorie item = find(id);
        if (item != null) {
            delete(item);
        }
    }

    public List<Categorie> find(Map<String, Object> params){
        List<Categorie> items = new ArrayList<>();
        try {
            items = dao.queryForFieldValues(params);
        } catch (SQLException e) {
            Log.e(TAG, "CategorieDao - FindMap - Error! ",e);
        }
        return items;
    }

}
