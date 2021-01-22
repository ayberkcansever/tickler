function OnUpdate(doc, meta) {
    if (meta.expiration == 0 ) return;
    var expiration = new Date(meta.expiration * 1000);
    var context = { id: meta.id, doc: doc };
    createTimer(DocTimerCallback, expiration, meta.id, context);
}

function DocTimerCallback(context) {
   try  {
        var request = {
            path: "/api/v1/tickle/couchbase",
            body: context
        };
       var response=curl("POST", tickler_url, request);
   } catch(e) {
       log('OnDelete: Exception:', e)
   }
}