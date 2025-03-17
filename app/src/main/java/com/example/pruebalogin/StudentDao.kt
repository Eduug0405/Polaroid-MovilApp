package com.example.pruebalogin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM student")
    fun getStudents(): Flow<List<Student>>

    @Query("DELETE FROM student")
    fun deleteAllStudents(): Int

    // Método para insertar un estudiante
    @Insert
    fun insertStudent(student: Student)
}
