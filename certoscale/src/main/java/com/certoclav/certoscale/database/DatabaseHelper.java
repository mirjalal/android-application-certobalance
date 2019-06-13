package com.certoclav.certoscale.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.certoclav.certoscale.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "certoclav.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 3;


    // the DAO object we use to access the SimpleData table

    private Dao<User, Integer> userDao = null;
    private Dao<Message, Integer> messageDao = null;
    private Dao<Library, Integer> libraryDao = null;
    private Dao<Recipe, Integer> recipeDao = null;
    private Dao<Item, Integer> itemDao = null;
    private Dao<Unit, Integer> unitDao = null;
    private Dao<Protocol, Integer> protocolDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, User.class);

            TableUtils.createTable(connectionSource, Message.class);
            TableUtils.createTable(connectionSource, Library.class);
            TableUtils.createTable(connectionSource, Recipe.class);
            TableUtils.createTable(connectionSource, Item.class);
            TableUtils.createTable(connectionSource, Unit.class);
            TableUtils.createTable(connectionSource, Protocol.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        try {
//            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
//            TableUtils.dropTable(connectionSource, User.class, true);
//            TableUtils.dropTable(connectionSource, Message.class, true);
//            TableUtils.dropTable(connectionSource, Library.class, true);
//            TableUtils.dropTable(connectionSource, Recipe.class, true);
//            TableUtils.dropTable(connectionSource, Item.class, true);
//            TableUtils.dropTable(connectionSource, Unit.class, true);
//            TableUtils.dropTable(connectionSource, Protocol.class, true);
//
//            // after we drop the old databases, we create the new ones
//            onCreate(db, connectionSource);//!
//        } catch (SQLException e) {
//            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
//            throw new RuntimeException(e);
//        }

        if (newVersion == 3) {
            db.execSQL("ALTER TABLE protocol ADD COLUMN is_uploaded INTEGER DEFAULT 0");
            db.execSQL("CREATE INDEX is_uploading_indexing ON protocol(is_uploaded)");
            db.execSQL("UPDATE protocol SET is_uploaded = 1 WHERE protocol_json like '%uploaded%'");
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */

    public Dao<User, Integer> getUserDao() {
        if (userDao == null) {
            try {
                userDao = getDao(User.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userDao;
    }


    public Dao<Item, Integer> getItemDao() {
        if (itemDao == null) {
            try {
                itemDao = getDao(Item.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return itemDao;
    }


    public Dao<Message, Integer> getMessageDao() {
        if (messageDao == null) {
            try {
                messageDao = getDao(Message.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return messageDao;
    }

    public Dao<Library, Integer> getLibraryDao() {
        if (libraryDao == null) {
            try {
                libraryDao = getDao(Library.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return libraryDao;
    }

    public Dao<Recipe, Integer> getRecipeDao() {
        if (recipeDao == null) {
            try {
                recipeDao = getDao(Recipe.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return recipeDao;
    }

    public Dao<Unit, Integer> getUnitDao() {
        if (unitDao == null) {
            try {
                unitDao = getDao(Unit.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return unitDao;
    }


    public Dao<Protocol, Integer> getProtocolDao() {
        if (protocolDao == null) {
            try {
                protocolDao = getDao(Protocol.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return protocolDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        userDao = null;
        messageDao = null;
        libraryDao = null;
        recipeDao = null;
        unitDao = null;
        itemDao = null;
        protocolDao = null;


    }


}
