# Real Time Sound Localization (Client)

This is the client application that runs on 4 Android devices and captures sound using the microphones. It then sends this data to the server application running on another android device which takes the data from all the 4 android devices and computes the X and Y coordinates of the sound source.

This method uses modified christian's algorithm to achieve time synchronization between the 4 devices. Modified christian's algorithm uses sound based communication between the devices to calculate offsets with accuracy upto 4ms.

Link to Server App : https://github.com/Premhalani/Sound-Localization-Server
