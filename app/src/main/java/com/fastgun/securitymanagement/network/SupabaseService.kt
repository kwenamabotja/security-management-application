package com.fastgun.securitymanagement.network

import io.supabase.gotrue.GoTrueClient
import io.supabase.gotrue.SignUpRequest
import io.supabase.postgrest.PostgrestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SupabaseService {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun signup(email: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): UserModel?
    suspend fun createVisitor(visitor: VisitorModel): Result<Unit>
    suspend fun getVisitors(): Result<List<VisitorModel>>
    suspend fun checkVehicleBlacklist(registration: String): Result<Boolean>
    suspend fun registerVehicle(vehicle: VehicleModel): Result<Unit>
    suspend fun reportIncident(incident: IncidentModel): Result<Unit>
    suspend fun logGateAccess(log: GateAccessLogModel): Result<Unit>
}

class SupabaseServiceImpl @Inject constructor(
    private val goTrueClient: GoTrueClient,
    private val postgrestClient: PostgrestClient
) : SupabaseService {

    override suspend fun login(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            goTrueClient.signInWithPassword(
                email = email,
                password = password
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            goTrueClient.signUp(
                email = email,
                password = password,
                redirectTo = "com.fastgun.securitymanagement://login"
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            goTrueClient.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): UserModel? = withContext(Dispatchers.IO) {
        try {
            val user = goTrueClient.currentUserOrNull()
            user?.let {
                UserModel(
                    id = it.id,
                    email = it.email ?: "",
                    createdAt = it.createdAt?.toString() ?: ""
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createVisitor(visitor: VisitorModel): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            postgrestClient
                .from("visitors")
                .insert(visitor)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVisitors(): Result<List<VisitorModel>> = withContext(Dispatchers.IO) {
        try {
            val response = postgrestClient
                .from("visitors")
                .select()
            Result.success(emptyList()) // Parse response appropriately
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkVehicleBlacklist(registration: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = postgrestClient
                .from("vehicles")
                .select()
                .eq("registration", registration)
                .eq("isBlacklisted", true)
            Result.success(true) // Check if result is empty or has data
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerVehicle(vehicle: VehicleModel): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            postgrestClient
                .from("vehicles")
                .insert(vehicle)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reportIncident(incident: IncidentModel): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            postgrestClient
                .from("incidents")
                .insert(incident)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logGateAccess(log: GateAccessLogModel): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            postgrestClient
                .from("gate_access_logs")
                .insert(log)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Data Models for API
data class UserModel(
    val id: String,
    val email: String,
    val createdAt: String
)

data class VisitorModel(
    val id: String = "",
    val firstName: String,
    val lastName: String,
    val idNumber: String,
    val email: String,
    val phone: String,
    val purpose: String,
    val company: String = "",
    val checkInTime: String = "",
    val checkOutTime: String = "",
    val isActive: Boolean = true
)

data class VehicleModel(
    val id: String = "",
    val registration: String,
    val make: String,
    val model: String,
    val color: String,
    val vin: String = "",
    val owner: String,
    val isBlacklisted: Boolean = false,
    val blacklistReason: String = ""
)

data class IncidentModel(
    val id: String = "",
    val type: String,
    val description: String,
    val location: String,
    val reportedBy: String,
    val timestamp: String = "",
    val status: String = "open",
    val severity: String = "medium"
)

data class GateAccessLogModel(
    val id: String = "",
    val vehicleRegistration: String,
    val driver: String,
    val accessType: String = "entry",
    val timestamp: String = "",
    val approvedBy: String
)
