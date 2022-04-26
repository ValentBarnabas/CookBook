# CookBook
Homework assignment for **Android-based software development** course at the *Budapest University of Technology and Economics*.

## Motivations

 I wanted to create an application for storing and sharing recipes in a cloud backed up system. The idea came to me after I broke one of my previous phones, and had lost all of my non-backed-up recipes, so I wanted a safer way for storing them. Then the idea came to extend it, so users can also share their recipes with others.

## Main Functions

- Registration and authentication
- Storage and visualisation of own and other's recipes
- Creation of recipes when the user is authenticated, saved both locally and in remote database
- "Liking" recipes each time the user makes them
- Deletion of the user's own recipes
- Pop-up windows to validate user input
- Sending notification to all users once a recipe reaches a certain amount of likes

## Explored Technologies

- Activity and Fragment classes
    - displaying different views
- Dialog Fragment
    - validation of user input
- Navigation Drawer Activity
    - handling of created fragments
- Livedata
    - displaying up-to-date data
- Firebase
    - registration and authentication
- Firestore
    - remote data storage
- Glide
    - displaying images stored online
- Firebase Messaging
    - push notifications
- Room API
    - masking SQLite requests
- Kotlinx Coroutines
    - asynchronous messages
- Retrofit
    - communication over HTTP
- Service
- Bundle
    - data transfer between fragments
- Parcelable
- ViewBinding
- Toast messages
- Extension functions
- Intent
- Staggered Grid Layout Manager
- Notification Manager

## Presentational images
- Opening fragment of application, Own Recipes Fragment. 
![Own Recipes Fragment](./documentation/1_loginless_myRecipes_framed.png)
- Navigation Drawer
![Navigation Drawer](./documentation/2_loginless_navDrawer_framed.png)
- Online Recipes Fragment
![Online Recipes Fragment](./documentation/3_loginless_onlineRecipes_framed.png)
- Opened Recipe Fragment
![Opened Recipe Fragment](./documentation/4_loginless_onlineRecipe_framed.png)
![End of Opened Recipe Fragment](./documentation/5_loginless_endOfOnlineRecipe_framed.png)
- Navigation Drawer with Authenticated Account
![Navigation Drawer with Authenticated Account](./documentation/7_login_navDrawer_NameAndLogout_framed.png)
- Creating New Recipe
![Creating New Recipe Fragment](./documentation/8_login_foodCreate_framed.png)
- Created Recipe View
![Created Recipe Fragment](./documentation/9_login_createdFoodMyRecipe_framed.png)
- Deleting Recipe Options
![Deleting Recipe Options](./documentation/10_login_createdFoodMyRecipeDelete_framed.png)
- Deleting Recipe Validation Popup
![Deleting Recipe Validation Popup](./documentation/11_login_createdFoodMyRecipeDeletePopup_framed.png)
- Push Notification
![Push Notification](./documentation/15_logout_popupMessage_framed.png)