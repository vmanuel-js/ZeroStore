package com.example.zero.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "busquedas")
data class Busqueda(
    @PrimaryKey val termino: String,
    val fechaHora: Long = System.currentTimeMillis()
)

@Dao
interface BusquedaDao {
    @Query("SELECT * FROM busquedas ORDER BY fechaHora DESC LIMIT 10")
    fun obtenerRecientes(): Flow<List<Busqueda>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(busqueda: Busqueda)

    @Query("DELETE FROM busquedas WHERE termino = :termino")
    suspend fun eliminar(termino: String)

    @Query("DELETE FROM busquedas")
    suspend fun limpiarTodo()
}

@Database(entities = [Busqueda::class], version = 2, exportSchema = false)
abstract class ZeroDatabase : RoomDatabase() {
    abstract fun busquedaDao(): BusquedaDao

    companion object {
        @Volatile private var INSTANCE: ZeroDatabase? = null

        fun getInstance(context: Context): ZeroDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ZeroDatabase::class.java,
                    "zero.db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build().also { INSTANCE = it }
            }
        }
    }
}