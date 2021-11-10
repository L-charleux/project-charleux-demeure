#Project Development of Mobile Application  - ISMIN 2021

Project developed by Loïc Charleux and Timothée Demeure for the course of '*Development of Mobile Application*' followed by students of Mines St Etienne, ISMIN - M2 Computer Science.

[![Mines St Etienne](./logo.png)](https://www.mines-stetienne.fr/)

## Content of the project

This project is separated into two parts:
* a NestJS app in `api` that is meant to be deployed on Clever Cloud 
* an Android app in `android`
 
###The API

The API uses a dataset created by us that is made up of Points of Interest (PoI) of the game Pokemon Go. There are 3 types of points of interest, Pokestops, Gyms and Golden Gyms that are marked in the app by different Markers.

Each element of the dataset has a set of attributes: {"Name", "Lieu", "GPS_Coord", "Badge", "Commentaires", "Niveau", "Type", "gx_media_links"}

Each attribute is pretty straightforward about its content except for `gx_media_links` that contains a URL of a screenshot of the PoI (be it a badge of it or a picture of it). The API also splits the coordinates into Latitude and Longitude, drops the attribute `Badge` and adds the `favorite` that is used by the app to put some PoIs as favorites.

###The App

The App is separated into 2 activities:
* The main activity is a TabLayout that holds 3 fragments:
  * A list of PoIs that is loaded from the API
  * A map using the Google Maps Android API
  * An info tab
* The details activity that loads more information about the PoIs and displays their picture

As stated, the main activity uses a TabLayout which allows the user to swipe between the 3 fragments.
It also has a refresh button that calls the API to refresh the content of the App (both the list and the map are refreshed)

The List is made of `row_poi` and that display the name and place of the PoI and a Favorite button that shows if the PoI is a favorite. The PoIs are sorted by favorite and by name. When the favorite star is clicked, it triggers a `PUT` to the API that changes the PoI to favorite and then refreshes the App.  

When an `row_poi` is clicked, it launches the details activity. This activity also features a favorite button and on exit (through the back button) of the activity, if the state of favorite changed, it sends the information to the main activity that triggers the change in the API.

The map is a classic map fragment that uses 3 sets of markers depending on the PoI.

Every icon used in the project is either a free use PNG or a PNG made by us, that were then converted to vector drawables (and modified by hand to be simpler).