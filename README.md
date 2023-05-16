# Die-No-Saur
Welcome to Die-No-Saur, a fun and addictive game where you play as a dinosaur and try to avoid obstacles to get the highest score possible. The game utilizes motion sensors to detect movement from the device in order to move the character sprite in-game.

Features
---------------
* Play as a Guest: You can play the game without registering by selecting the Guest option. You can pause the game or mute/unmute the sound at any time during the game.
* Register: To save your scores and progress, you can register for an account. The registration form has error checking to ensure that your email and password are legitimate and match.
* Login: Once you have registered, you can log in to access your saved data.
* Logout: You can log out at any time to protect your data.
* Settings: You can customize the game with your own preferences, such as the character sprite and game background.
* Play: Start a new game and try to beat your high score.
* Retry: If you lose the game, you can retry it with the same settings.
* Share: You can share your score on Facebook by connecting your account.
* Leaderboard: See where you rank compared to other players who have registered.

APIs and Services Used
---------------
* Sensor Manager Service: This service provides access to and manages the sensors in the application, which allows the user to change the game's state based on their condition.
* Canvas Service: This service is used to draw the assets used in the game portion of the application.
* Facebook API: This API is used to allow access to a user's Facebook account to share their game scores.
* Firebase Realtime Database Service: This service is used to record and compare user's scores with other scores.
