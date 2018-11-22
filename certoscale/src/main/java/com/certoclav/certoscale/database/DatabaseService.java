package com.certoclav.certoscale.database;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * CertoClavDatabase class is responsible for the communication of the
 * application with the sqlite database.
 *
 * @author Iulia Rasinar <iulia.rasinar@nordlogic.com>
 */
public class DatabaseService {
    private final String TAG = getClass().getSimpleName();
    private final Context mContext;


    Dao<User, Integer> userDao;
    Dao<Message, Integer> messageDao;
    Dao<Library, Integer> libraryDao;
    Dao<Recipe, Integer> recipeDao;
    Dao<Item, Integer> itemDao;
    Dao<Unit, Integer> unitDao;
    Dao<Protocol, Integer> protocolDao;

    private DatabaseHelper mDatabaseHelper;


    /**
     * Constructor
     *
     * @param context the context of calling application
     */
    public DatabaseService(final Context context) {
        this.mContext = context;
        mDatabaseHelper = getHelper();


        userDao = getHelper().getUserDao();
        messageDao = getHelper().getMessageDao();
        libraryDao = getHelper().getLibraryDao();
        recipeDao = getHelper().getRecipeDao();
        itemDao = getHelper().getItemDao();
        unitDao = getHelper().getUnitDao();
        protocolDao = getHelper().getProtocolDao();


    }

    /**
     * Releases the helper when done.
     */
    public boolean isEmpty(){
        return getProtocols().size() == 0;
    }


    public void close() {
        if (mDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            mDatabaseHelper = null;
        }
    }

    public boolean isInDatabase(String name){
        for (Protocol protocol : getProtocols()){
            if (protocol.getAshBeakerName().equals(name) && protocol.getIsPending()) return true;
        }
        return false;
    }

    public Protocol getRecentProtocol(){
//        if (getProtocols().size() > 0){
//            for(Protocol protocol: getProtocols())
//                if(protocol.getIsPending())
//                    return protocol;
//        }
        return null;
    }


    public List<Message> getMessages() {
        try {

            /** query for object in the database with id equal profileId */
            return messageDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }


    public int insertMessage(Message message) {

        try {

            int x = messageDao.create(message);


            return x;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return -1;
    }

    public int deleteMessage(final Message message) {
        try {

            return messageDao.delete(message);
        } catch (java.sql.SQLException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }
        return -1;
    }


    public List<Library> getLibraries() {
        try {

            /** query for object in the database with id equal profileId */
            return libraryDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }


    public int insertLibrary(Library library) {

        try {

            int x = libraryDao.create(library);


            return x;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return -1;
    }

    public int deleteLibrary(final Library library) {
        try {

            return libraryDao.delete(library);
        } catch (java.sql.SQLException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }
        return -1;
    }


    public List<Item> getItems() {
        try {

            /** query for object in the database with id equal profileId */
            List<Item> items = itemDao.queryForAll();
            for (Item item : items) {
                item.parseJson();
            }
            return items;
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }

    public Item getItemById(int itemId) {
        try {

            Item item = itemDao.queryForId(itemId);
            item.parseJson();
            return item;
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }


    public Unit getUnitbyId(int unitID) {
        try {

            Unit unit = unitDao.queryForId(unitID);
            unit.parseUnitJson();
            return unit;
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }


    public int insertItem(Item item) {

        item.generateJson();
        try {

            int x = itemDao.create(item);


            return x;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return -1;
    }

    public int deleteItem(final Item item) {
        try {

            return itemDao.delete(item);
        } catch (java.sql.SQLException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }
        return -1;
    }


    public List<Unit> getUnits() {
        try {
            List<Unit> units = new ArrayList<Unit>();
            /** query for object in the database with id equal profileId */
            units = unitDao.queryForAll();
            for (Unit unit : units) {
                unit.parseUnitJson();
            }
            return units;
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }


    public int insertProtocol(Protocol protocol) {

        protocol.generateJson();
        try {
            protocol.generateJson();
            int x = protocolDao.create(protocol);
            return x;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return -1;
    }

    public int deleteProtocol(Protocol protocol) {
        try {

            return protocolDao.delete(protocol);
        } catch (java.sql.SQLException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }
        return -1;
    }


    public List<Protocol> getProtocols() {
        try {
            return protocolDao.queryForAll();

        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return new ArrayList<>();
    }

    public List<Protocol> getPengingProtocols() {
        try {
            List<Protocol> protocols = protocolDao.queryForEq("protocol_is_pending",true);
            return protocols;

        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return new ArrayList<>();
    }


    public int insertUnit(Unit unit) {

        unit.generateUnitJson();
        try {

            int x = unitDao.create(unit);
            return x;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return -1;
    }

    public int deleteUnit(Unit unit) {
        try {

            return unitDao.delete(unit);
        } catch (java.sql.SQLException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }
        return -1;
    }


    public List<Recipe> getRecipes() {
        try {

            /** query for object in the database with id equal profileId */
            List<Recipe> recipes = recipeDao.queryForAll();
            try {
                for (Recipe recipe : recipes) {
                    recipe.parseJson();
                }
            } catch (Exception e) {

            }
            return recipes;
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }

    public Recipe getRecipeById(int recipeId) {
        try {

            Recipe recipe = recipeDao.queryForId(recipeId);
            recipe.parseJson();
            return recipe;
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }


    public int insertRecipe(Recipe recipe) {

        recipe.generateJson();

        try {

            int x = recipeDao.create(recipe);


            return x;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return -1;
    }

    public int deleteRecipe(final Recipe recipe) {
        try {

            return recipeDao.delete(recipe);
        } catch (java.sql.SQLException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }
        return -1;
    }


    public List<User> getUsers() {
        try {

            /** query for object in the database with id equal profileId */
            return userDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {

            Log.e(TAG, "Database exception", e);

        }

        return null;
    }

    public User getUserById(int userId) {
        try {

            return userDao.queryForId(userId);
        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }

    public List<User> getUserByCloudId(String userId) {
        try {
            return userDao.queryBuilder().where().eq(User.FIELD_USER_CLOUD_ID, userId).query();

        } catch (SQLException e) {
            Log.e(TAG, "Database exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Database exception", e);
        }

        return null;
    }

    public int insertUser(User user) {

        try {
            return userDao.create(user);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * Get the helper from the manager once per class.
     */
    private DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = OpenHelperManager.getHelper(mContext,
                    DatabaseHelper.class);
        }
        return mDatabaseHelper;
    }

    public int updateUserVisibility(String email, boolean isVisible) {
        try {
            UpdateBuilder<User, Integer> updateBuilder = userDao
                    .updateBuilder();

            updateBuilder.where().eq("email", email);

            /** query for object in the database with id equal profileId */
            updateBuilder.updateColumnValue("is_visible", isVisible);

            int r = updateBuilder.update();
            return r;
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public int updateUserIsLocal(String email_user_id, boolean isLocal) {
        try {
            UpdateBuilder<User, Integer> updateBuilder = userDao
                    .updateBuilder();

            updateBuilder.where().eq("email", email_user_id);

            /** query for object in the database with id equal profileId */
            updateBuilder.updateColumnValue(User.FIELD_USER_LOCAL, isLocal);

            int r = updateBuilder.update();
            return r;
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public int deleteUser(User user) {

        try {
            return userDao.delete(user);
        } catch (java.sql.SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }


    public List<User> getUsersWhereVisible() {
        try {

            return userDao.queryBuilder().where().eq("is_visible", true).query();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();

        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        return null;


    }


    public int updateItemCloudId(Item item, String cloudId) {
        try {
            item.setCloudId(cloudId);
            deleteItem(item);
            int r = insertItem(item);
            return r;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;

    }


    public int updateRecipeCloudId(Recipe recipe, String cloudId) {
        try {
            recipe.setCloudId(cloudId);
            deleteRecipe(recipe);
            int r = insertRecipe(recipe);
            return r;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;

    }

    public int updateProtocolCloudId(Protocol protocol, String cloudId) {
        try {
            protocol.setCloudId(cloudId);
            deleteProtocol(protocol);
            int r = insertProtocol(protocol);
            return r;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;

    }

    public int updateUserPassword(String email_user_id, String newPassword) {
        try {
            UpdateBuilder<User, Integer> updateBuilder = userDao
                    .updateBuilder();

            updateBuilder.where().eq("email", email_user_id);

            /** query for object in the database with id equal profileId */
            updateBuilder.updateColumnValue("password", newPassword);

            int r = updateBuilder.update();
            return r;
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;

    }


}