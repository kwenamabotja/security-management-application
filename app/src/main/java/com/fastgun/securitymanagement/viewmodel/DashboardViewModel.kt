package com.fastgun.securitymanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastgun.securitymanagement.network.SupabaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val supabaseService: SupabaseService
) : ViewModel() {

    sealed class DashboardState {
        object Idle : DashboardState()
        object Loading : DashboardState()
        data class Success(
            val userName: String,
            val activeVisitors: Int,
            val recentIncidents: Int
        ) : DashboardState()
        data class Error(val message: String) : DashboardState()
    }

    private val _dashboardState = MutableStateFlow<DashboardState>(DashboardState.Idle)
    val dashboardState: StateFlow<DashboardState> = _dashboardState

    fun loadDashboardData() {
        viewModelScope.launch {
            try {
                _dashboardState.value = DashboardState.Loading

                // Get current user
                val currentUser = supabaseService.getCurrentUser()
                val userName = currentUser?.email?.split("@")?.get(0) ?: "Officer"

                // Get active visitors (mock data for now)
                val visitors = supabaseService.getVisitors()
                val activeVisitorsCount = if (visitors.isSuccess) 0 else 0

                // Get recent incidents (mock data for now)
                val recentIncidentsCount = 0

                _dashboardState.value = DashboardState.Success(
                    userName = userName,
                    activeVisitors = activeVisitorsCount,
                    recentIncidents = recentIncidentsCount
                )
            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                supabaseService.logout()
                _dashboardState.value = DashboardState.Idle
            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error(e.message ?: "Logout failed")
            }
        }
    }
}
