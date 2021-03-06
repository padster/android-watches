#Custom Android watchfaces, simplifed

### What

Want to write custom watch faces for your Android watch, but get confused or run into problems when trying to read the
[guide](https://developer.android.com/training/wearables/watch-faces/index.html])?
These are the shortcuts I made when writing my own, aimed at people trying to just write nice looking watches 
(i.e. no fancy features), to simplify things into just:

* A way to lazily create resources when starting
* A way to draw an active watchface (updated every second)
* A way to draw a passive watchface (updated every minute)

### How

1. Check out the code into Android studio, make sure it builds
1. Put your watch on its charging dock, and connect that via USB.
1. Turn on USB debugging (note: don't use bluetooth debugging unless you really have to...).
1. Deploy the 'wear' app to your watch.
1. Enjoy!
1. Try writing your own SimpleWatchface.

Ask me any questions about the API or help with debugging,
the upcoming plans are all at SimpleWatchface.java

