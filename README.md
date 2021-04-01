Android application that pulls repositories from all group members(3) git users
- When a user clicks on a repository the app display all the commits made on that repository
- The application only sends the initial requests once every 24 hours, other calls use cached responses
- Application implement MVVM architecture and uses RxJava observables for displaying the data.
- Application uses one activity with multiple fragments. All tied to the same view model
- All recycler view items are animated
- Animated splash screen.
- CardView is used across UI
- Orientation locked to portrait
- Git/Firebase Authentication - oAuth (Through the use of Intent)
