# Security Management Application

A mobile application for fast gun security personnel to manage gate access, scan South African driver's licenses and vehicle disks, log visitor information, and report incidents.

## Features

✅ **Security Personnel Authentication** - Secure login/logout for authorized staff
✅ **QR Code Scanning** - Scan SA driver's licenses and vehicle disks using ML Kit
✅ **Visitor Management** - Register, track, and check-in/out visitors
✅ **Vehicle Tracking** - Database of registered vehicles with blacklist capability
✅ **Gate Access Logging** - Audit trail of all gate entries/exits
✅ **Incident Reporting** - Document security incidents with timestamps
✅ **Offline-First** - Local storage with cloud sync when connected
✅ **PDF Reports** - Export visitor logs and incidents to PDF
✅ **Cloud Sync** - Real-time synchronization with Supabase

## Tech Stack

- **Platform**: Android (Kotlin)
- **Local Database**: Room Database (SQLite)
- **Cloud Database**: Supabase (PostgreSQL)
- **Authentication**: Supabase Auth
- **QR Scanning**: Google ML Kit Vision
- **PDF Generation**: iText
- **API Communication**: Retrofit
- **Dependency Injection**: Hilt
- **Async**: Kotlin Coroutines

## Project Structure

```
security-management-application/
├── app/src/main/java/com/fastgun/securitymanagement/
│   ├── ui/                    # Android UI Activities & Fragments
│   ├── viewmodel/             # ViewModels for MVVM architecture
│   ├── repository/            # Data access layer
│   ├── database/              # Room Database & DAOs
│   ├── network/               # Supabase API client
│   ├── model/                 # Data models
│   ├── utils/                 # Utility functions
│   └── MainActivity.kt        # Entry point
├── app/build.gradle           # App dependencies
├── build.gradle               # Project configuration
└── README.md                  # This file
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or higher
- Supabase account
- Android device or emulator (API 24+)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/kwenamabotja/security-management-application.git
cd security-management-application
```

2. **Configure Supabase**
   - Create a Supabase project
   - Copy your API keys from Supabase dashboard
   - Create `local.properties` file in project root:
```properties
supabase.url=YOUR_SUPABASE_URL
supabase.anon.key=YOUR_SUPABASE_ANON_KEY
```

3. **Build and Run**
   - Open in Android Studio
   - Sync Gradle files
   - Run on device/emulator

## Core Modules

### Authentication
- Login/logout for security personnel
- JWT token management
- Session persistence

### QR Scanner
- Real-time camera preview
- ML Kit QR code detection
- Parse SA driver's license data
- Extract vehicle disk information

### Visitor Management
- Check-in/out tracking
- Visitor profile creation
- Purpose of visit documentation
- Contact information storage

### Incident Reporting
- Incident type categorization
- Photo/evidence attachment
- Incident notes and timestamps
- Status tracking (open/closed/resolved)

### Reporting & Export
- Generate PDF reports
- Email integration for sharing
- Date range filtering
- Export to cloud storage

## API Endpoints (Supabase)

- `POST /auth/v1/token` - User authentication
- `POST /rest/v1/visitors` - Create visitor record
- `GET /rest/v1/visitors` - Fetch visitor logs
- `POST /rest/v1/vehicles` - Register vehicle
- `GET /rest/v1/vehicles?blacklisted=true` - Check blacklist
- `POST /rest/v1/incidents` - Report incident
- `POST /rest/v1/gate_access_logs` - Log gate access

## Database Schema

### Tables
- `users` - Security personnel
- `visitors` - Visitor information
- `vehicles` - Vehicle registry
- `gate_access_logs` - Access audit trail
- `incidents` - Security incidents
- `sync_queue` - Offline sync tracking

## Development

### Build APK
```bash
./gradlew assembleRelease
```

### Run Tests
```bash
./gradlew test
```

### Generate Documentation
```bash
./gradlew dokka
```

## Security Considerations

⚠️ **Important Security Notes:**
- All data is encrypted in transit (TLS/SSL)
- Sensitive data is stored securely using Android KeyStore
- Authentication tokens are refreshed automatically
- Offline data is encrypted locally
- All API calls use Supabase Row Level Security policies

## Deployment

### Deploy to Google Play Store
1. Generate release keystore
2. Sign APK with keystore
3. Upload to Google Play Console

### Deploy Backend to Vercel (Later Stage)
- Supabase handles cloud infrastructure
- Can add custom backend API on Vercel if needed
- Use Supabase Edge Functions for custom logic

## Troubleshooting

### QR Scanner Not Working
- Check camera permissions in AndroidManifest.xml
- Ensure device has Google Play Services installed
- Test with different lighting conditions

### Cloud Sync Issues
- Verify Supabase credentials in local.properties
- Check internet connectivity
- Review sync_queue table for failed records

### Authentication Failed
- Verify user credentials in Supabase Auth dashboard
- Check JWT token expiration
- Review Firebase/Supabase logs

## Contributing

1. Create a feature branch
2. Make your changes
3. Submit a pull request
4. Ensure all tests pass

## License

This project is proprietary and confidential for Fast Gun Security.

## Support

For issues and questions:
- Create an issue on GitHub
- Contact: [your-email@fastgun.com]

---

**Last Updated**: May 2026
**Version**: 1.0.0-alpha
