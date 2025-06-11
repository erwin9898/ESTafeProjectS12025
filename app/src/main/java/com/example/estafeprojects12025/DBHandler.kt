package com.example.estafeprojects12025

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASENAME, factory, DBVERSION) {

        // Declare Tablename and fields
            private val tableName = "Person"
            private val keyID = "ID"
            private val keyName = "NAME"
            private val keyMobile = "MOBILE"
            private val keyEmail = "EMAIL"
            private val keyAddress = "ADDRESS"
            private val keyImageFile = "IMAGEFILE"

        companion object{
            const val DATABASENAME="HRManager.db"
            const val DBVERSION = 2
        }
    override fun onCreate(db: SQLiteDatabase) {
        // create sql statement for table
        val createTable = "CREATE TABLE $tableName($keyID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$keyName TEXT, $keyImageFile TEXT, $keyAddress TEXT, $keyMobile TEXT, $keyEmail TEXT)"
        //create sql statement
        db.execSQL(createTable)
        // add one sample record using contentValue object
        val cv = ContentValues()
        cv.put(keyName,"Erwin Sandero")
        cv.put(keyMobile, "0401234567")
        cv.put(keyAddress, "Sydney")
        cv.put(keyImageFile, "first")
        cv.put(keyEmail, "erwin@tafemail.com")
        // use insert method to add
        db.insert(tableName, null,cv)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $tableName")
        // recreate database
        onCreate(db)
    }
    // get all the records from database
    fun getAllPersons():ArrayList<Person>{
        // declare an ArrayList
        val personList= ArrayList<Person>()
        // create sql query
        val selectQuery = "SELECT * FROM $tableName"
        // get readable database
        val db = this.readableDatabase
        // run query and put result in cursor
        val cursor = db.rawQuery(selectQuery,null)
        //move through cursor
        if(cursor.moveToFirst()){
            //loop to read all records
            do{
                // create a person object
                val person = Person()
                person.id = cursor.getInt(0)
                person.name = cursor.getString(1)
                person.imageFile = cursor.getString(2)
                person.address = cursor.getString(3)
                person.mobile = cursor.getString(4)
                person.email = cursor.getString(5)
                // add to arraylist
                personList.add(person)
            }while (cursor.moveToNext())
        }
        //class cursor and db
        cursor.close()
        db.close()
        //return arraylist
        return personList
    }

    fun getEmployee(id: Int): Person {
        // get readable database
        val db = this.readableDatabase
        // create employee object to fill details
        val employee = Person()
        //create query cursor
        val cursor = db.query(tableName,
            arrayOf(keyID,keyName,keyImageFile,keyAddress,keyMobile,keyEmail),
            "$keyID=?",
            arrayOf(id.toString()),
            null,null,null)

        if(cursor!=null){
            cursor.moveToFirst()
            employee.id = cursor.getInt(0)
            employee.name = cursor.getString(1)
            employee.imageFile = cursor.getString(2)
            employee.address = cursor.getString(3)
            employee.mobile = cursor.getString(4)
            employee.email = cursor.getString(5)

        }

        // close cursor and db
        cursor.close()
        db.close()
        // return employee object with details
        return employee

    }

    fun updateEmployee(employee: Person) {
        // get writeable db
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(keyName,employee.name)
        cv.put(keyAddress,employee.address)
        cv.put(keyEmail,employee.email)
        cv.put(keyMobile,employee.mobile)
        cv.put(keyImageFile,employee.imageFile)
        db.update(tableName,cv,"$keyID=?", arrayOf(employee.id.toString()))
        // db close
        db.close()
    }
    fun addEmployee(employee: Person) {
        // get writeable db
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(keyName, employee.name)
        cv.put(keyAddress, employee.address)
        cv.put(keyEmail, employee.email)
        cv.put(keyMobile, employee.mobile)
        cv.put(keyImageFile, employee.imageFile)
        db.insert(tableName, null, cv)
        // close db
        db.close()

    }

    fun deleteEmployee(id: Int) {
        // get writable db
        val db = this.writableDatabase
        db.delete(tableName,"$keyID=?", arrayOf(id.toString()))
        // close the db
        db.close()

    }

}
