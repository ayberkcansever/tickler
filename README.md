


Tickler
-----------------
Firstly let's define the need for Tickler: 

***I am, as a Tickler client application, running in distributed environment and i need an expiring distributed data structure which all of the items in this structure may have different time-to-live values.***

These expiring items (JSON based) can be called as reminders (tickles) which are composed of a callback url or callback kafka topic, json payload for the callback and a ttl value in seconds.

Main components of a sample environment are stated below:

<img src="https://i.ibb.co/rdN3Z5G/tickler-4.png"/>

The flow of the diagram:

 1. A client posts a tickle via a rest call or publishes a tickle to a Kafka topic. The expiry requested tickle contains a rest callback url or a callback Kafka topic which will be triggered with the payload after the expiration. A tickle contains a time-to-live value (in seconds) beside the callbak info. Payload message is the data which will be sent back to the tickle client after the expiration.
 2. 
 * If Redis Background Enabled: A tickler instance which receives the tickle request sets two key-value pairs onto REDIS. One of them is for receiving the expired key and the other is receiving the expired value. Two key-value pair must be set because REDIS gives only the key of the record when it expires, not both key and value.
 * If Couchbase Background Enabled: A tickler instance which receives the tickle request inserts the tickle into a couchbase bucket with the requested ttl value. An eventing process, which is the core functionality of Couchbase cluster, sets a Couchbase timer after this insertion. The timer function runs when ttl expires and calls an endpoint of a Tickler instance which will distribute the expired tickle to be processed among tickler instances.
 3. One of the tickler instances are stated as the leader for distributing the expired tickle if REDIS background is enabled. That is because REDIS pushes the key expiration events to all subscribed instances. If REDIS background is enabled, the expired tickle key is received by all tickler instances but only the leader initiates the distribution of the expired tickle among the Tickler instances (Kafka pub-sub). The leader gets the tickle value from REDIS after receiving the expired tickle key. On the other hand if Couchbase background is enabled, there is no need for leader election because Couchbase timer function makes an only one rest call to tickler instances.
 4. After having the expired tickle value, the tickler instance (it is the leader if REDIS background is enabled or any of tickler instances if Couchbase background is enabled) sends it to a specific Kafka topic (tickle.process) which is consumed by all the Tickler instances. This is done for spreading the tickle processing load on all tickler instances. All the tickler instances are bound to this internal topic with the same group id and the load is distributed among all tickler instances.
 5. Any of tickler instances receives the tickle value from internal Kafka topic (tickle.process). Now it has all the values to finish the process.
 6. It is time to call the client's callback url with the corresponding payload or send it to a callback Kafka topic which is provided by the tickle client. The client is informed with the payload data of the tickle after the expiration.
