@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1')
import groovy.transform.Field
import groovyx.net.http.HTTPBuilder
import org.apache.http.auth.*

import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

@Field final http = new HTTPBuilder( 'https://api.taiga.io' )

@Field final proxyHost = ""
@Field final proxyPort = 80
@Field final proxyUser = ""
@Field final proxyPwd  = ""

// Configure fields and uncomment below lines to allow access through proxy
/*
http.client.getCredentialsProvider().setCredentials(
    new AuthScope( proxyHost, proxyPort ),
    new UsernamePasswordCredentials( proxyUser, proxyPwd )
)

http.setProxy( proxyHost, proxyPort, 'http')
*/

return http