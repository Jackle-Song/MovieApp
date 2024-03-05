package com.mrsworkshop.movieapp.mySQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mrsworkshop.movieapp.apidata.response.MovieListDetails

class MovieListDatabaseHandler(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
){
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MovieListDatabase"

        // MARK : For FYK Detail Info
        const val KEY_MOVIE_LIST_DETAIL = "movieListTable"
        const val MOVIE_ID = "movieId"
        const val KEY_MOVIE_LIST_VO = "movieListDetailsVO"
    }

    override fun onCreate(sqliteDB: SQLiteDatabase?) {
        val createInspectorFykDetailContentTable = ("CREATE TABLE " + KEY_MOVIE_LIST_DETAIL + "("
                + MOVIE_ID + " TEXT PRIMARY KEY,"
                + KEY_MOVIE_LIST_VO + " TEXT" + ")")
        sqliteDB?.execSQL(createInspectorFykDetailContentTable)
    }

    override fun onUpgrade(sqliteDB: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        sqliteDB?.execSQL("DROP TABLE IF EXISTS $KEY_MOVIE_LIST_DETAIL")
        onCreate(sqliteDB)
    }

    // MARK : For Adding Movie List Details Item
    fun addNewMovieListDetailItem(movieID: String, movieListDetails: MovieListDetails) : Long {
        val db = this.readableDatabase
        val contentValues = ContentValues().apply {
            put(MOVIE_ID, movieID)
            val movieListItemInputString : String = Gson().toJson(movieListDetails)
            put(KEY_MOVIE_LIST_VO, movieListItemInputString)
        }

        // Inserting Row
        val success = try {
            db.insert(KEY_MOVIE_LIST_DETAIL, null, contentValues)
        }
        catch (e : Exception) {
            -1
        }
        db.close()

        return success
    }

    // MARK : For Retrieving Full List of Movie Details Item List from Local Database
    fun retrieveLocalMovieListItem() : MutableList<MovieListDetails> {
        val localMovieList : MutableList<MovieListDetails> = mutableListOf()
        val selectQuery = "SELECT  * FROM $KEY_MOVIE_LIST_DETAIL"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT * FROM $KEY_MOVIE_LIST_DETAIL", null)
        }
        catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return localMovieList
        }

        if (cursor.moveToFirst()) {
            do {
                val movieListDetailInputString : String? = cursor.getString(cursor.getColumnIndex("movieListDetailsVO") ?: 0)

                val jsonObj : JsonObject = JsonParser.parseString(movieListDetailInputString).asJsonObject
                val parsedMovieList = Gson().fromJson(jsonObj, MovieListDetails::class.java)
                localMovieList.add(parsedMovieList)

            } while (cursor.moveToNext())
        }

        db.close()
        return localMovieList
    }

    fun updateLocalMovieListDetailItem(movieID: String, movieListDetails: MovieListDetails) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(MOVIE_ID, movieID)
            val movieListInputString : String = Gson().toJson(movieListDetails)
            put(KEY_MOVIE_LIST_VO, movieListInputString)
        }

        // FOR Updating Row Data
        val whereClause = "$MOVIE_ID=?"
        val whereArgs = arrayOf(movieID)
        val success = db.update(
            KEY_MOVIE_LIST_DETAIL, contentValues,
            whereClause, whereArgs)
        db.close() // Closing database connection
        return success
    }
}