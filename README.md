# Myhealthapp

## Overview

Myhealthapp is an Android application designed to help users manage their health and wellness. The app allows users to log their food intake, set daily health targets, and view a dashboard summarizing their daily activities. It is built using Java and integrates with Firebase for authentication and data storage.

## Features

### User Authentication
- Users can log in using their email and password.
- Firebase authentication is used for secure login.

### Dashboard
- Displays a pie chart showing the user's daily food consumption and remaining daily limit.
- Uses MPAndroidChart library for data visualization.

### Food Logging
- Users can log their food intake through various methods:
  - Manual entry
  - Camera capture
  - Voice commands

### Health Targets
- Users can set daily health targets like caloric intake and exercise goals.

### Data Storage
- All data is stored securely in Firebase Firestore.

## Installation

1. Clone the Repository
    ```bash
    git clone https://github.com/mishra-mihir/Myhealthapp.git
    ```

2. Open the project in Android Studio.
3. Sync the Gradle files and build the project.
4. Run the app on an Android emulator or physical device.

## Dependencies

- Android SDK
- Firebase Authentication
- Firebase Firestore
- MPAndroidChart

## Contributing

Feel free to fork the project and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under general Open Soouce License.


