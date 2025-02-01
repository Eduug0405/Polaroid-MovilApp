
package com.example.pruebalogin.repository

import com.example.pruebalogin.model.LoginRequest
import com.example.pruebalogin.model.LoginResponse
import com.example.pruebalogin.network.RetrofitClient
import retrofit2.Response

class LoginRepository {
    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return RetrofitClient.apiService.login(request)
    }
}
