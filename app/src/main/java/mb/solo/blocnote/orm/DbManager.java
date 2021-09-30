package mb.solo.blocnote.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import mb.solo.blocnote.model.Categorie;
import mb.solo.blocnote.model.Note;

public class DbManager extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "blocnote.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TAG = "orm";

    public DbManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Note.class);
            TableUtils.createTable(connectionSource, Categorie.class);
            Log.i(TAG, "Création table OK!");
        }catch (Exception e){
            Log.e(TAG, "Create Error! ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Note.class, true);
            TableUtils.dropTable(connectionSource, Categorie.class, true);
            onCreate(database, connectionSource);
            Log.i(TAG, "Update table OK!");
        }catch (Exception e){
            Log.e(TAG, "Update Error! ", e);
        }
    }
}
