# BBWeather Architeture

## Presentation layer
MVVM and LiveData from Android Architeture Components. It uses one activity with multiple fragments. The views trigger the actions to the ViewModels, and Views also observe the data changes from the ViewModel.

The design of the ViewModel in this project is pretty lightweight, it delegates the requests from Views to the Domain layer.  So it keeps the business logic out of presention layer.

## Domain layer
Domain layer is responsible for the business logic. In the project there are several mangers to run the logic asynchronously / synchronously. Those managers simply run the logic, and then tell the callers with the status (success or failed) via callbacks.

## Data layer
There are three type of data in this project.
- Weather data
- User bookmarked locations
- User preference

To weather data is on the cloud, the project uses async tasks to retrieve it. And bookmarked locations are stored locally in the device, since there are not a lot of data, so this project uses the file to store it instead of other heavier solutions. And user preference data is store in the SharedPreferences.

## Communication
There are two majoy communications in the project.
- Reactive programming.
- Callback

The **LiveData** from **Android Architeture Components** provides a reactive way for data communication. This is main communication beween the **View** and **ViewModel**. Although it is not as powerful as **RxJava**, but for a small project like this, it is powerful enough. 

The project also uses **Callback** to do the interaction back to the caller. **ViewModel** uses it to talk the the domain layer. 
