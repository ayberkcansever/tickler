function OnUpdate(doc, meta) {
    // Only process for those documents that have a non-zero TTL
    if (meta.expiration == 0 ) return;
    var oneSecondPrior = new Date(meta.expiration * 1000);
    var context = { id: meta.id, doc : doc };
    createTimer(DocTimerCallback, oneSecondPrior , meta.id, context);
}

function DocTimerCallback(context) {
   try  {
        var request = {
            path: "/api/v1/tickle/couchbase/expire",
            body: context.doc
        };

       var response=curl("POST", tickler_url, request);
       log(context);
   } catch(e) {
       log('OnDelete: Exception:', e)
   }
}