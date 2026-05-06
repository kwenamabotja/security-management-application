package com.fastgun.securitymanagement.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.LinearLayout
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fastgun.securitymanagement.R
import com.fastgun.securitymanagement.ui.auth.LoginActivity
import com.fastgun.securitymanagement.ui.scanner.QRScannerActivity
import com.fastgun.securitymanagement.ui.visitor.VisitorManagementActivity
import com.fastgun.securitymanagement.ui.incident.IncidentReportActivity
import com.fastgun.securitymanagement.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private lateinit var greetingText: TextView
    private lateinit var activeVisitorsCard: TextView
    private lateinit var recentIncidentsCard: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var scanButton: Button
    private lateinit var visitorButton: Button
    private lateinit var incidentButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initializeViews()
        setupClickListeners()
        observeDashboardState()

        // Load dashboard data
        dashboardViewModel.loadDashboardData()
    }

    private fun initializeViews() {
        greetingText = findViewById(R.id.greeting_text)
        activeVisitorsCard = findViewById(R.id.active_visitors_card)
        recentIncidentsCard = findViewById(R.id.recent_incidents_card)
        progressBar = findViewById(R.id.progress_bar)
        scanButton = findViewById(R.id.scan_button)
        visitorButton = findViewById(R.id.visitor_button)
        incidentButton = findViewById(R.id.incident_button)
        logoutButton = findViewById(R.id.logout_button)
    }

    private fun setupClickListeners() {
        scanButton.setOnClickListener {
            startActivity(Intent(this, QRScannerActivity::class.java))
        }

        visitorButton.setOnClickListener {
            startActivity(Intent(this, VisitorManagementActivity::class.java))
        }

        incidentButton.setOnClickListener {
            startActivity(Intent(this, IncidentReportActivity::class.java))
        }

        logoutButton.setOnClickListener {
            performLogout()
        }
    }

    private fun observeDashboardState() {
        lifecycleScope.launch {
            dashboardViewModel.dashboardState.collect { state ->
                when (state) {
                    is DashboardViewModel.DashboardState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is DashboardViewModel.DashboardState.Success -> {
                        progressBar.visibility = View.GONE
                        greetingText.text = "Welcome, ${state.userName}!"
                        activeVisitorsCard.text = "Active Visitors\n${state.activeVisitors}"
                        recentIncidentsCard.text = "Recent Incidents\n${state.recentIncidents}"
                    }
                    is DashboardViewModel.DashboardState.Error -> {
                        progressBar.visibility = View.GONE
                        greetingText.text = "Error loading data"
                    }
                    is DashboardViewModel.DashboardState.Idle -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun performLogout() {
        dashboardViewModel.logout()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
