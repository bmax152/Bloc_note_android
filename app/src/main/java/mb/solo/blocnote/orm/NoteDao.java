package mb.solo.blocnote.orm;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import mb.solo.blocnote.model.Note;

public class NoteDao {
    private static final String TAG = "orm";
    private static Dao<Note, Integer> dao = null;

    public NoteDao(Context context){
        if(dao == null){
            try {
                DbManager db = new DbManager(context);
                dao = db.getDao(Note.class);
            } catch (SQLException e) {
                Log.e(TAG, "NoteDao - Error! ",e);
            }
        }
    }

    /**
     * CRUD
     */

    public List<Note> list(){
        List<Note> items = new ArrayList<>();
        try {
            //items = dao.queryForAll();
            items = dao.queryBuilder().orderBy("dateNote", false).query();
            Log.i(TAG, "NoteDao - SelectAll - OK");
        } catch (SQLException e) {
            Log.e(TAG, "NoteDao - SelectAll - Error! ",e);
        }
        return items;
    }

    public Note find(int id){
        try {
            Log.i(TAG, "NoteDao - Find OK");
            return dao.queryForId(id);
        } catch (SQLException e) {
            Log.e(TAG, "NoteDao - Find-Error! ",e);
            return null;
        }
    }

    public void create(Note item){
        try {
            dao.create(item);
            Log.i(TAG, "NoteDao - Create OK");
        } catch (SQLException e) {
            Log.e(TAG, "NoteDao - Create - Error! ",e);
        }
    }

    public void update(Note item){
        try {
            dao.update(item);
            Log.i(TAG, "NoteDao - Update OK");
        } catch (SQLException e) {
            Log.e(TAG, "NoteDao - Update - Error! ",e);
        }
    }

    public void createOrUpdate(Note item){
        try {
            dao.createOrUpdate(item);
            Log.i(TAG, "NoteDao - Create or Update OK");
        } catch (SQLException e) {
            Log.e(TAG, "NoteDao - Create or Update - Error! ",e);
        }
    }

    public void delete(Note item){
        try {
            dao.delete(item);
            Log.i(TAG, "NoteDao - Delete OK");
        } catch (SQLException e) {
            Log.e(TAG, "NoteDao - Delete - Error! ",e);
        }
    }

    public void delete(int id) {
        Note item = find(id);
        if (item != null) {
            delete(item);
        }
    }

    public List<Note> find(Map<String, Object> params){
        List<Note> items = new ArrayList<>();
        try {
            items = dao.queryForFieldValues(params);
        } catch (SQLException e) {
            Log.e(TAG, "UserDao - FindMap - Error! ",e);
        }
        Collections.sort(items);
        return items;
    }

    public List<Note> findCategorieNull(){
        List<Note> items = new ArrayList<>();
        try{
            items = dao.queryBuilder().orderBy("dateNote", false).where()
                    .isNull("categorie_id_categories")
                    .query();
        }catch (SQLException e) {
            Log.e(TAG, "UserDao - FindNull - Error! ",e);
        }
        return items;
    }

    public List<Note> findText(String search){
        List<Note> items = new ArrayList<>();
        try{
            items = dao.queryBuilder().orderBy("dateNote", false).where()
                    .like("texte", "%"+search+"%")
                    .query();
        }catch (SQLException e) {
            Log.e(TAG, "UserDao - FindText - Error! ",e);
        }
        return items;
    }
}
