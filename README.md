# Counter App

A simple yet powerful Android application to help you keep track of anything you want, built with a modern, reactive architecture.

## About The Project

This Counter App is designed to provide a user-friendly and feature-rich experience for creating, managing, and tracking various counters. It is built using modern Android development practices, emphasizing a clean, scalable, and maintainable codebase.

## Key Features

*   **Create and Manage Counters:** Easily create new counters with custom names, colors, and initial values.
*   **Categorize Counters:** Organize your counters into categories for better management.
*   **Increment and Decrement:** Quickly increment or decrement counter values with a single tap.
*   **History Tracking:** View the history of all counter actions.
*   **Customization:** Personalize the app with different themes and settings.
*   **Data Backup and Restore:** Backup your data and restore it whenever you need.

## Technologies Used

This project is built with modern Android development technologies:

*   **Kotlin:** The primary programming language for building the app.
*   **Coroutines & StateFlow:** For asynchronous programming and managing state in a reactive way. The UI observes `StateFlow` from the ViewModels to get the latest state.
*   **Android Jetpack:** A suite of libraries to help developers follow best practices.
    *   **View Binding:** To easily write code that interacts with views.
    *   **ViewModel:** To store and manage UI-related data in a lifecycle-conscious way.
    *   **Room Persistence Library:** An abstraction layer over SQLite for robust local database access.
    *   **Navigation Component:** To handle all aspects of in-app navigation.
    *   **WorkManager:** To manage deferrable, asynchronous background tasks.
    *   **DataStore:** A data storage solution for storing key-value pairs or typed objects.
*   **Hilt - Dagger:** For dependency injection, simplifying the app's architecture and improving testability.
*   **Material Components for Android:** To implement Material Design for a high-quality user experience.

## Architecture

The app follows a clean architecture pattern, separating concerns into three main layers: UI, Domain, and Data. This promotes a modular, testable, and maintainable codebase.

### 1. UI Layer

This layer is responsible for displaying the application data on the screen and handling user interactions.

*   **Views (Fragments & Activities):** The UI is built using Fragments. They are responsible for observing the state from the ViewModel and rendering it. They capture user interactions and pass them to the ViewModel as Actions.
*   **ViewModel:** Each screen has a corresponding ViewModel that holds the UI state. It receives actions from the View, processes them (often by calling a use case from the Domain layer), and updates the UI state.
*   **UI State (`StateFlow`):** The ViewModel exposes a `StateFlow` of a `UiState` data class. This state is an immutable object that represents everything needed to render the screen at any given moment.
*   **Actions & Events:** User interactions are modeled as `Actions` (e.g., `CreateCounterAction`). These are sent to the ViewModel. The ViewModel can then send one-time `Events` (e.g., `ShowToastEvent`, `NavigateToScreenEvent`) back to the UI for things like showing a Snackbar or navigating to another screen. This creates a unidirectional data flow, making the app's logic easier to follow and debug.

### 2. Domain Layer

The Domain layer contains the core business logic of the application. It is independent of the UI and Data layers, making it reusable and easy to test.

*   **Use Cases (Interactors):** These are the core business logic units. Each use case has a single responsibility (e.g., `CreateCounterUseCase`, `GetAllCountersUseCase`). They are called by the ViewModels in the UI layer. They orchestrate the flow of data from the repositories in the Data layer.
*   **Domain Models:** These are plain Kotlin data classes that represent the core entities of the application (e.g., `Counter`, `Category`). They are used by all layers but are defined in the domain layer, as they represent the business objects themselves, independent of how they are stored or displayed.

### 3. Data Layer

The Data layer is responsible for providing the data required by the application. It abstracts the data sources from the rest of the app.

*   **Repositories:** The repositories are the single source of truth for the app's data. They expose data to the Domain layer (via Use Cases). The repository implementation (e.g., `CounterRepositoryImpl`) decides whether to fetch data from a local database or a remote source (though this app primarily uses a local source).
*   **Data Sources:**
    *   **Room Database (DAOs):** For structured, persistent data, the app uses the Room persistence library. Data Access Objects (DAOs) define the SQL queries to interact with the underlying SQLite database.
    *   **DataStore:** For simple key-value data like user preferences, the app uses Jetpack DataStore.
*   **Entities:** These are data classes that represent the structure of the data in the database (e.g., `CounterEntity`). They are often annotated with Room-specific annotations.
*   **Mappers:** Mapper classes are used to convert data between layers. For example, a `CounterMapper` would convert a `CounterEntity` object from the Data layer into a `Counter` domain model for the Domain layer to use.

## App Screens

Here is a breakdown of the app's screens and their functionalities:

### Home Screen

This is the main screen of the app. It provides a quick overview of your most important counters and categories.

![Home Screen](https://via.placeholder.com/300x500.png?text=Home+Screen)

### Counters List Screen

This screen displays a list of all your counters. You can view, edit, or delete counters from this screen.

![Counters List Screen](https://via.placeholder.com/300x500.png?text=Counters+List)

### Categories List Screen

This screen shows a list of all your categories. You can manage your categories from here.

![Categories List Screen](https://via.placeholder.com/300x500.png?text=Categories+List)

### Create Counter Screen

This screen allows you to create a new counter. You can set the name, initial value, color, and category for the counter.

![Create Counter Screen](https://via.placeholder.com/300x500.png?text=Create+Counter)

### Create Category Screen

This screen allows you to create a new category. You can set the name and color for the category.

![Create Category Screen](https://via.placeholder.com/300x500.png?text=Create+Category)

### Counter View Screen

This screen provides a detailed view of a single counter. You can increment, decrement, and reset the counter from here. You can also view the history of the counter.

![Counter View Screen](https://via.placeholder.com/300x500.png?text=Counter+View)

### Category View Screen

This screen displays all the counters belonging to a specific category.

![Category View Screen](https://via.placeholder.com/300x500.png?text=Category+View)

### History Screen

This screen shows a log of all counter activities, such as increments, decrements, and resets.

![History Screen](https://via.placeholder.com/300x500.png?text=History)

### Settings Screen

This screen allows you to customize the app's settings, such as the theme, notifications, and data backup options.

![Settings Screen](https://via.placeholder.com/300x500.png?text=Settings)

### About Screen

This screen provides information about the app, such as the version number and developer details.

![About Screen](https://via.placeholder.com/300x500.png?text=About)

## Installation

To build and run the app, follow these steps:

1.  **Clone the repository:**
    ```
    git clone https://github.com/droidevs/counter-app.git
    ```
2.  **Open in Android Studio:** Open the project in Android Studio.
3.  **Build the project:** Let Android Studio sync and build the project.
4.  **Run the app:** Run the app on an emulator or a physical device.
