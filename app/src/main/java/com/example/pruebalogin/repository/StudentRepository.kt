package com.example.pruebalogin.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StudentRepository(private val studentDao: StudentDao) {

    fun getStudents(): Flow<List<String>> {
        return studentDao.getStudents().map { students ->
            students.map { it.name }
        }
    }

    suspend fun removeAllStudents() {
        withContext(Dispatchers.IO) {
            studentDao.deleteAllStudents()
        }
    }

    suspend fun insertStudent(name: String) {
        withContext(Dispatchers.IO) {
            studentDao.insertStudent(Student(name = name))
        }
    }
}
