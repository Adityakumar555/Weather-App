# Weather App
This Weather App provides real-time weather data based on a user’s location or a searched city. It uses the OpenWeather API to fetch weather details such as temperature, humidity, wind speed, and weather condition. The app also allows the user to search for weather by city name and provides a refreshing mechanism to update the data. The app follows the MVVM (Model-View-ViewModel) architecture for a clean separation of concerns and better maintainability.

# Features
* **Current Location Weather:** Automatically fetches the user's current location using GPS and displays weather information.
* **City Search:** Allows users to manually input a city name to get weather data.
* **Swipe-to-Refresh:** Enables the user to refresh weather data by swiping the screen.
* **Weather Details:** Displays temperature (in Celsius), humidity, wind speed, weather description, and the city name.
* **Permission Handling:** Requests location and internet access permissions, and gracefully handles permission denial by showing relevant prompts and dialogs.
* **Responsive UI:** The app’s UI is designed to be clean and intuitive, displaying the weather information clearly.

# Technologies and Tools Used
* **Kotlin:** Programming language used for building the app.
* **MVVM Architecture:** Model-View-ViewModel pattern to separate the concerns of UI and business logic.
* **Retrofit:** Used to interact with the OpenWeather API.
* **Google Play Services Location:** To fetch the user’s location.
* **LiveData and ViewModel:** For observing data changes in a lifecycle-aware manner.

# Usage

* Open the app, it will try to fetch the current weather based on your device's location.
* You can search for weather by typing the city name in the search bar and clicking the search icon to fetch the weather data.
* You can refresh the weather data by swiping the screen down.
* The weather details will include temperature (in Celsius), humidity, wind speed, and general weather description.

# App Video
