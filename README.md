# Clean Our Community

## Description

Clean Our Community is an Android application built with Jetpack Compose to connect volunteers with local clean-up events.  The app aims to make community service more accessible and engaging by providing a platform for users to discover, join, and organize environmental clean-up initiatives.

**Key Features:**

*   **Event Discovery:** Browse upcoming clean-up events on a map or list, filterable by location and keywords.
*   **Event Creation:**  Users can easily host their own clean-up events, specifying details like date, time, location, description, and volunteer capacity.
*   **Volunteer Sign-up:**  Users can join events, manage their participation, and track their contribution.
*   **User Profiles:**  Personalized profiles to showcase user activity, including points earned and total cleanups completed.
*   **Friend System:** Connect with friends, send friend requests, and build a community of environmental advocates.
*   **Leaderboard:**  Gamified experience with a leaderboard to recognize top contributors and encourage participation.
*   **Points and Recognition:** Earn points for participating in events, contributing to a global impact score.
*   **Google Maps Integration:**  Interactive map to visualize event locations and aid in discovery.
*   **User Authentication:** Secure user login and signup via Firebase Authentication.

## Technologies Used

*   **Programming Language:** [Kotlin](https://kotlinlang.org/)
*   **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Android SDK:**  Targeting Android API 35
*   **Build System:** [Gradle](https://gradle.org/) with Kotlin DSL
*   **Database & Backend:** [Firebase](https://firebase.google.com/)
    *   [Firebase Authentication](https://firebase.google.com/docs/auth)
    *   [Firebase Firestore](https://firebase.google.com/docs/firestore)
    *   [Firebase Storage](https://firebase.google.com/docs/storage)
    *   [Firebase Analytics](https://firebase.google.com/docs/analytics)
*   **Mapping:** [Google Maps Compose](https://developers.google.com/maps/documentation/android-compose)
*   **Permissions Handling:** [Accompanist Permissions](https://google.github.io/accompanist/permissions/)
*   **Image Loading:** [Coil-kt](https://coil-kt.github.io/coil/)
*   **JSON Parsing:** [Gson](https://github.com/google/gson)
*   **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
*   **UI Components:** [Material Design 3](https://m3.material.io/)

## Installation Instructions

To set up and run the Clean Our Community application locally, follow these steps:

**Prerequisites:**

1.  **Android Studio:**  Download and install the latest version of [Android Studio](https://developer.android.com/studio).
2.  **Java Development Kit (JDK):** Ensure you have JDK 1.8 or higher installed. Android Studio typically bundles an appropriate JDK.
3.  **Firebase Project:**
    *   Create a new project on the [Firebase Console](https://console.firebase.google.com/).
    *   Register an Android app in your Firebase project, using `"com.example.myapplication"` as the package name.
    *   Download the `google-services.json` file and place it in the `app/` directory of your project.
4.  **Google Maps API Key:**
    *   Enable the "Maps SDK for Android" for your Firebase project in the Google Cloud Console.
    *   Create an API key in the Google Cloud Console credentials section, restricting it to Android apps and your app's package name and SHA-1 signing certificate fingerprint (for release builds, you'll need to add your release certificate fingerprint as well).
    *   You will need to obtain an API key for the Maps SDK for Android.

**Setup Steps:**

1.  **Clone the Repository:**
    ```bash
    git clone [repository URL]
    cd clean-master
    ```
    *(Replace `[repository URL]` with the actual URL of your project repository.)*

2.  **Open in Android Studio:** Open Android Studio and select "Open an existing project". Navigate to the cloned `clean-master` directory and open the project.

3.  **Configure API Keys:**
    *   Create a file named `secrets.properties` in the `app/` directory.
    *   Add the following line to `secrets.properties`, replacing `YOUR_MAPS_API_KEY` with the API key you obtained in the prerequisites:
        ```properties
        MAPS_API_KEY=YOUR_MAPS_API_KEY
        ```
        Alternatively, you can create a `local.defaults.properties` file in the same directory for default values.
    *   **Important:** Ensure `secrets.properties` (or `local.defaults.properties` if you used it) is properly ignored by your version control system (e.g., added to `.gitignore`) to avoid committing your API key.

4.  **Sync Gradle:**  After opening the project, Android Studio will prompt you to sync Gradle files. Click "Sync Now" or "Sync Project with Gradle Files" in the toolbar.

5.  **Build and Run:**
    *   Connect an Android emulator or a physical Android device to your development machine.
    *   In Android Studio, select "Run 'app'" from the toolbar or use the shortcut `Shift+F10` (or `Ctrl+R` on macOS).
    *   Choose your emulator or device as the deployment target.

## Usage Guide

Once the application is installed and running, you can:

1.  **Sign Up or Log In:**  If you are a new user, sign up using your email and password on the Login screen. Existing users can log in with their credentials.
2.  **Explore Events:** Navigate to the "Events" screen from the navigation drawer. You will see a map displaying upcoming clean-up events. You can also switch to a list view below the map to browse events.
3.  **Search Events:** Use the search bar at the top of the Events screen to search for events by title or description.
4.  **View Event Details:** Tap on an event marker on the map or an event item in the list to view detailed information about the event, including the host, attendees, date, time, location, description, and points awarded.
5.  **Join/Unjoin Events:** On the event details screen, you can tap the "Join" or "Unjoin" button to manage your participation in the event.
6.  **Create Events:** To host your own clean-up event, navigate to "My Events" from the navigation drawer and tap the "Host a Clean-up" button. Fill in the event details in the "Create Event" form.
7.  **Manage My Events:**  In the "My Events" screen, you can view and edit events you have created.
8.  **View Past Events:** Navigate to "Past Events" from the navigation drawer to see a list of completed events.
9.  **Leaderboard:** Check the "Leaderboard" screen to see the top contributors and the global impact progress bar.
10. **Friends:**  Go to the "Friends" screen to manage your friends and send friend requests to other users.
11. **Profile:** Access your profile screen from the navigation drawer to view your user information, friend requests, and edit your description.
