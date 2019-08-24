package com.esafirm.androidplayground.flipper

import android.content.Context
import androidx.room.*

@Database(entities = [FlipperData::class], version = 1)
abstract class FlipperDatabase : RoomDatabase() {
    abstract fun flipperDao(): FlipperDao
}

@Dao
interface FlipperDao {

    @Query("SELECT * FROM flipper_data")
    fun getAll(): List<FlipperData>

    @Insert
    fun insert(data: FlipperData)
}

@Entity(tableName = "flipper_data")
class FlipperData {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
    var name: String = ""
}

class DatabaseHelper(context: Context) {

    private val dao: FlipperDao by lazy {
        Room.databaseBuilder(context, FlipperDatabase::class.java, "flipper_db")
            .allowMainThreadQueries()
            .build()
            .flipperDao()
    }

    fun insertData(data: FlipperData) {
        dao.insert(data)
    }

    fun getAll(): List<FlipperData> = dao.getAll()
}
