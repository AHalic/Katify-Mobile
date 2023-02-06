# Katify Mobile
This is an android version of a Kanban project called [Katify](https://github.com/AHalic/Katify).
It consists of an annotation app, on which the notes can be separate into different pastes (here called kanbans, though they are not exactly a kanban). 
The app was developped to help people organize their tasks.

## Screens


## Features
The features implemented are listed bellow:
- Login with Google
- Create Kanbans clicking on the cat plus button
- Create Note to a kanban clicking on the plus button inside the kanban
- Delete kanbans and notes by swiping the card
- Change kanbans and notes names
- Write note content

The app is also implemented in two languages: english and french. This is automatically selected based on the system language.


## Implementation
Here are some informations about the implementation of the app.

It was made using Kotlin and the mvvm pattern. 
[Firebase](https://firebase.google.com) was used to save the login, which can only be made using a google account. 
The profile's photo is loaded using the [Picasso](https://square.github.io/picasso/) library.

Recycler Views were used to list both the kanbans and the notes. And the app contains a local database to store all the kanbans and its notes.

## Future Features
- [ ] Note tags
- [ ] Rich text to Note content
- [ ] Firebase storage to every data (so data can be accessed from different devices)

### [Mockup](https://www.figma.com/file/wZwqL7hR6S7H1BaQAJb3UO/Katify-Mobile?node-id=0%3A1&t=BnqIHhWK5iGcvwzV-0)
