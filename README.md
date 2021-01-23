

Tickler
-----------------
Firstly we define the need for Tickler: 

***I am, as a tickle client application, running in distributed environment and need an expiring distributed data structure which all the items in this structure can have different time-to-live values.***

These expiring json items can be called as reminders (tickles) which are composed of a callback url, json payload for the callback and a ttl value in seconds.

Main components of a sample environment are stated below:

<img src="https://i.ibb.co/4ZgNm75/tickler-2.png"/>

The main flow of the diagram:

 1. A client posts a tickle containing a call back url which will be called after the tickle expires. A tickle also contains a payload message and a time-to-live value in seconds. 
 2. If Redis Enabled: A tickler instance which receives the tickle request sets two key-value pairs onto REDIS. One of them is for receiving expired key and the other is receiving the expired value. Two keys are needed because REDIS gives only the key when it expires not both key and value.
 If Couchbase Enabled: A tickler instance which receives the tickle request inserts the tickle into a couchbase bucket with ttl. An eventing process sets a Couchbase timer after the insertion. The timer runs when ttl expires and calls an endpoint from a Tickler instance which will process the expired tickle.
 3. One of the tickler instances are stated as the leader for processing the expired tickle. That is because REDIS pushes the key expiration events to all subscribed instances. The expired tickle key is received by all tickler instances but only the leader processes it. The leader gets the tickle value from REDIS after receiving the key.
 4. After having the expired tickle value, the leader sends it to a Kafka topic. This is for spreading the load on all tickler instances. All the tickler instances are bound to this topic with the same group id and the load is distributed among all tickler instances.
 5. Any of tickler instances receives the tickle value from Kafka topic. Now it has all the values to finish the process.
 6. It is time to call the client's callback url with the corresponding payload. The client is informed that the tickle, which is sent in the 1st step by the client itself, is expired. Now the client can do anything it wants with the custom expired payload.
