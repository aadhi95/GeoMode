import android.database.sqlite.SQLiteDatabase;


/**
 * Created by aadhithya on 1/5/17.
 */

public class dbconnect {
    SQLiteDatabase db;

    dbconnect(SQLiteDatabase d) {
        db = d;
        db.execSQL("create if not exists modetable(mid varchar primary key,)");
        db.execSQL("create if not exists alertable(lat varchar,long varchar, radius varchar, mode varchar )");
    }
}
