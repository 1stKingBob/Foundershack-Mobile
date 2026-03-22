# ScoutHub - Sports Talent Acquisition Mobile App

A comprehensive Android application for sports talent acquisition, connecting athletes with scouts and clubs. Built with Android Studio and Supabase backend.

## Features

### Athlete Profile
- **Home Feed**: Search clubs, scouts, and players; view stories and posts
- **Chats**: Messaging system with filtering options
- **Reels**: Short video content (under 45 seconds) with like/save functionality
- **Calendar**: Event management for trials, training, matches, and meetings
- **Profile**: Personal information, achievements, stats, and reel gallery

### Scout Profile
- **Home Feed**: View posts and trials from clubs and athletes
- **Chats**: Same messaging system as athletes
- **Reels**: Upload and manage video content
- **Calendar**: Track scouting events and meetings
- **Profile**: Scout information and promising players overview

### Key Features
- **User Authentication**: Secure login/signup with email/password
- **Real-time Messaging**: Chat system between users
- **Video Recording**: In-app camera for recording reels
- **Calendar Integration**: Add trials and events to personal calendar
- **Profile Management**: Edit personal information and statistics
- **Content Sharing**: Upload photos, videos, stats, and articles

## Technical Stack

### Frontend
- **Android Studio** with Java
- **Material Design Components**
- **RecyclerView** for efficient list rendering
- **ExoPlayer** for video playback
- **CameraX** for video recording
- **Glide** for image loading

### Backend
- **Supabase** for:
  - Authentication
  - Database (PostgreSQL)
  - Storage (for media files)
  - Real-time updates

### Architecture
- **MVVM Pattern** with fragments
- **Repository Pattern** for data management
- **Coroutines** for asynchronous operations

## Setup Instructions

### 1. Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 24+
- Java 8+

### 2. Supabase Setup
1. Create a new Supabase project at [supabase.com](https://supabase.com)
2. Run the `database_schema.sql` file in your Supabase SQL editor
3. Update `SupabaseConfig.java` with your project URL and anon key:
   ```java
   private static final String SUPABASE_URL = "https://your-project-id.supabase.co";
   private static final String SUPABASE_ANON_KEY = "your-anon-key";
   ```

### 3. Android Setup
1. Clone this repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Update your `build.gradle` with your package name
5. Run the app

## Database Schema

The app uses the following main tables:
- `users`: User profiles (athletes and scouts)
- `posts`: Social media posts and trial announcements
- `stories`: Instagram-style stories
- `calendar_events`: Personal calendar events
- `chats` & `messages`: Messaging system
- `reels`: Short video content
- `promising_players`: Scout's list of promising athletes
- `user_stats`: Athletic statistics
- `achievements`: User achievements

## Security Features

- **Row Level Security (RLS)** enabled on all tables
- **JWT Authentication** via Supabase Auth
- **Input validation** on all user inputs
- **Secure file storage** with access controls

## Color Scheme

The app uses a green and white color scheme inspired by the ScoutHub brand:
- Primary Green: `#4CAF50`
- Light Green: `#E8F5E9`
- Orange Accent: `#FFA726`
- Dark Grey: `#333333`
- Light Grey: `#F0F0F0`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please contact the development team or open an issue in the repository.

---

**Note**: This is a demonstration project. For production use, ensure proper security measures, testing, and compliance with app store guidelines.
