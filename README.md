Tickler
-----------------
Firstly we define the need for Tickler: 

I am, as an application, running in distributed environment and need an expiring distributed set which all the items in the set can have different time-to-live values.

These expiring items can be called as reminders (tickles) which are composed of a callbackUrl, payload json for the callback and a ttl value in seconds.

Main components of an sample environment are stated below:

<img src="https://i.ibb.co/bb8Swkk/tickler-1.png"/>

The main flow of the diagram:

 1. A client posts a tickle which is containing a call back url that will be called after the tickle expires. A tickle also contains a payload message and a time-to-live value in seconds. 
 2. A tickler instance which receives the tickle request sets two key-value pairs onto REDIS. One of them is receiving expired key and the other is receiving the expired value. Two keys are needed because REDIS gives only the key when it expires not both key and value. 
 3. One of the tickler instances are stated as the leader for processing the expired tickle. That is because REDIS pushes the key expiration events to all subscribed instances. The expired tickle key is received by all tickler instances but only the leader processes it. The leader gets the tickle value from REDIS after receiving the key.
 4. After having the expired tickle value, the leader sends it to a Kafka topic. This is for spreading the load on all tickler instances. All the tickler instances are bound to this topic with the same group id and the load is distributed among all tickler instances.
 5. Any of tickler instances receives the tickle value from Kafka topic. Now it has all the values to finish the process.
 6. It is time to call the client's callback url with the corresponding payload. The client is informed that the tickle, which is sent in the 1st step by the client itself, is expired. Now the client can do anything it wants with the custom expired payload.
