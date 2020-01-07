# MyWeather

An Android app, written in Java. It is made for the Udemy course, The Complete Android N Developer Course by Rob Percival. The goal is to make a weather app that gives the information about the weather situation
in a city that the user asks for. Also, I did a variation and I added the information about the temperature.

# What I learned

* Developed a UI within the activity_main.xml using RelativeLayout, TextView, EditText and Button.
* Implemented functionality to the UI using: onCreate, AsyncTask and onClick (sendRequest).
* Integrated doInBackground and onPostExecute to open a connection and with the help of an InputStream, fetch the data through the API call.
* Used JSONObject to manipulate the fetched data and return the desired result to the user in a TextView.