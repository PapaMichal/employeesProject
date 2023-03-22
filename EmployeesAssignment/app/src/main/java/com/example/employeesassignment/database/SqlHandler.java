package com.example.employeesassignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.employeesassignment.Employee;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SqlHandler extends SQLiteOpenHelper {
    private static SqlHandler instance;
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String EXPERTISE_FIELDS = "ExpertiseFields";
    private static final String EMPLOYEES = "Employees";
    private static final String EMP_NAME = "name";
    private static final String EMP_HOURLY_PAY = "hourly_pay";
    private static final String EMP_ADDRESS = "address";
    private static final String EMP_PICTURE = "picture";
    private static final String EMP_EDUCATION = "education";
    private static final String EMP_EMPLOYMENT_RATE = "employment_rate";
    private static final String EMPLOYEES_NAME = EMPLOYEES + ".name";
    private static final String EXPERTISE_FIELDS_NAME = EXPERTISE_FIELDS + ".fieldName";

    private SqlHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (CreateTableSql sql : CreateTableSql.values()) { //Creates all tables
            db.execSQL(sql.toString());
        }
        db.execSQL("INSERT OR IGNORE INTO ExpertiseFields (fieldName) VALUES ('Regulation'), ('Quality'), ('Digital Health'), ('V&V'), ('IVD');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Table table : Table.values()) {
            db.execSQL("DROP TABLE IF EXISTS" + table.toString() + ";");
        }
    }

    @Override
    public synchronized void close() {
        if (instance != null)
            close();
    }

    public static synchronized SqlHandler getInstance(Context context) {
        if (instance == null) {
            instance = new SqlHandler(context);
        }
        return instance;
    }

    private Cursor getColByCol(String table, String colToGet, String colCriteria, String colCriteriaVal) {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(
                table,   // The table to query
                new String[]{colToGet},
                colCriteria + "=?",
                new String[]{colCriteriaVal},
                null,
                null,
                null
        );
    }


    private boolean doesRowExist(String tableName, String colName, String colVal) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(
                tableName,
                new String[]{colName},
                colName + "=?",
                new String[]{colVal},
                null, null, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean doesEmployeeExist(String name) {
        return doesRowExist(EMPLOYEES, EMPLOYEES_NAME, name);
    }

    private Cursor getAllRowsOfCol(String table, String col) {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(table, new String[]{col},
                null, null, null, null, null);
    }

    private String getIdByCol(String table, String colCriteria, String colCriteriaVal) {
        Cursor cursor = getColByCol(table, "id", colCriteria, colCriteriaVal);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        return Integer.toString(id);
    }

    public String getEmailByUsername(String username) {
        Cursor cursor = getColByCol("Users", "email", "username", username);
        cursor.moveToFirst();
        String email = cursor.getString(0);
        cursor.close();
        return email;
    }

    private void insertEmployeeExpertise(String employeeId, String fieldName) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO EmployeesExpertise (expertise_id, employee_id) " +
                      "VALUES ((SELECT id FROM ExpertiseFields WHERE fieldName=?), ?);";
        db.execSQL(sql, new String[]{fieldName, employeeId});
    }

    private void insertEmployeeExpertises(String employeeId, ArrayList<String> fieldNames) {
        for (String fieldName: fieldNames) {
            insertEmployeeExpertise(employeeId, fieldName);
        }
    }

    private Bitmap blobToBitmap(byte[] blob) {
        if (blob == null) {
            return null;
        } else {
            return BitmapFactory.decodeByteArray(blob, 0, blob.length);
        }
    }

    private static byte[] bitmapToBlob(Bitmap bitmap) {
        if (bitmap == null){
            return null;
        }
        else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        }
    }

    public ArrayList<Employee> getEmployees() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM Employees";
        int id = 0;
        db.rawQuery(sql, new String[]{});
        Cursor cursor = db.query(EMPLOYEES, null, null, null, null, null, null);
        ArrayList<Employee> employees = new ArrayList<>();
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            employees.add(new Employee(
                    cursor.getString(cursor.getColumnIndexOrThrow(EMP_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EMP_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EMP_EDUCATION)),
                    getEmployeeFieldsOfExpertise(id),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EMP_EMPLOYMENT_RATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(EMP_HOURLY_PAY)),
                    blobToBitmap(cursor.getBlob(cursor.getColumnIndexOrThrow(EMP_PICTURE)))
                    )
            );
        }
        cursor.close();
        return employees;
    }

    public boolean addEmployee(String name, String address, String education, int hourlyPay, int employmentRate, ArrayList<String> expertises, Bitmap picture) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("education", education);
        values.put("hourly_pay", hourlyPay);
        values.put("employment_rate", employmentRate);
        values.put("picture", bitmapToBlob(picture));
        db.beginTransaction();
        boolean isInsertSuccessful = false;
        try
        {
            if (!doesEmployeeExist(name))
            {
                db.insert(EMPLOYEES, null, values);
                if (doesEmployeeExist(name)) {
                    String employeeId = getIdByCol(EMPLOYEES, "name", name);
                    insertEmployeeExpertises(employeeId, expertises);
                    db.setTransactionSuccessful();
                    return true;
                }
            }
            return false;

        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally{
            db.endTransaction();
        }
    }

    public String[] getFieldsOfExpertise(){
        Cursor cursor = getAllRowsOfCol(EXPERTISE_FIELDS, EXPERTISE_FIELDS_NAME);
        ArrayList<String> names = new ArrayList<String>();
        while (cursor.moveToNext()) {
            names.add(cursor.getString(0));
        }
        cursor.close();
        return names.toArray(new String[0]);
    }

    private ArrayList<String> getEmployeeFieldsOfExpertise(int employeeId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql =
                "SELECT fieldName\n" +
                "FROM ExpertiseFields\n" +
                "JOIN EmployeesExpertise\n" +
                "ON ExpertiseFields.id = EmployeesExpertise.expertise_id\n" +
                "WHERE EmployeesExpertise.employee_id = " + Integer.toString(employeeId) + ";";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<String> fieldsOfExpertise = new ArrayList<String>();
        while (cursor.moveToNext()) {
            fieldsOfExpertise.add(cursor.getString(0));
        }
        cursor.close();
        return fieldsOfExpertise;
    }

    public boolean register(String user, String pass, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", user);
        cv.put("pass", pass);
        cv.put("email", email);
        long res = db.insert("Users", null, cv);
        return res != -1;
    }
    public Cursor getData(String sql){
        return this.getWritableDatabase().rawQuery(sql, null);
    }

    private boolean doesExist(String selQuery) {
        String sql = "SELECT EXISTS(" + selQuery + " LIMIT 1)";
        Cursor cursor = getData(sql);
        cursor.moveToFirst();
        boolean doesASingleResultExist = cursor.getInt(0) == 1;
        cursor.close();
        return doesASingleResultExist;
    }

    private boolean doesExist(String table, String col, String val) {
        return doesExist("SELECT * FROM " + table + " WHERE " + col + "=" + "'" + val + "'");
    }

    public boolean doesUserExist(String username) {
        return doesExist("Users", "username", username);
    }
    public boolean doesEmailExist(String email) {
        return doesExist("Users", "email", email);
    }

    public boolean isPasswordCorrect(String username, String pass) {
        return doesExist("SELECT * FROM Users WHERE username='" + username + "' AND pass='" + pass + "'");
    }
}
