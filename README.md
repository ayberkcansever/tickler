


Tickler
-----------------
Firstly we define the need for Tickler: 

***I am, as a tickle client application, running in distributed environment and need an expiring distributed data structure which all of the items in this structure may have different time-to-live values.***

These expiring items (JSON based) can be called as reminders (tickles) which are composed of a callback url or message topic, json payload for the callback and a ttl value in seconds.

Main components of a sample environment are stated below:

<img src="https://i.ibb.co/yP5mNsF/tickler-3.png"/>

The main flow of the diagram:

 1. A client posts a tickle containing a call back url or Kafka topic which will be triggered after the tickle expires. A tickle contains a payload message and a time-to-live value in seconds beside the callbak info.
 2. 
 * If Redis Enabled: A tickler instance which receives the tickle request sets two key-value pairs onto REDIS. One of them is for receiving the expired key and the other is receiving the expired value. Two keys are needed because REDIS gives only the key when record expires, not both key and value.
 * If Couchbase Enabled: A tickler instance which receives the tickle request inserts the tickle into a couchbase bucket with the requested ttl value. An eventing process, which is the core functionality of Couchbase cluster, sets a Couchbase timer after this insertion. The timer function runs when ttl expires and calls an endpoint of a Tickler instance which will process the expired tickle.
 3. One of the tickler instances are stated as the leader for processing the expired tickle. That is because REDIS pushes the key expiration events to all subscribed instances. If REDIS background is enabled, the expired tickle key is received by all tickler instances but only the leader processes it. The leader gets the tickle value from REDIS after receiving the key. On the other hand if Couchbase background enabled there is no need for leader election because Couchbase timer function makes an only one rest call to tickler instances.
 4. After having the expired tickle value, the tickler instance (it is the leader if REDIS background enabled or any of tickler instances if Couchbase background enabled) sends it to a Kafka topic. This is done for spreading the tickle processing load on all tickler instances. All the tickler instances are bound to this topic with the same group id and the load is distributed among all tickler instances.
 5. Any of tickler instances receives the tickle value from Kafka topic. Now it has all the values to finish the process.
 6. It is time to call the client's callback url with the corresponding payload or send it to a Kafka topic. The client is informed that the tickle, which is sent in the beginning of the flow by the client itself, is expired. Now the client has the expired payload.
