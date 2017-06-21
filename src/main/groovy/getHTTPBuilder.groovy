@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1')
import groovy.transform.Field
import groovyx.net.http.HTTPBuilder
import org.apache.http.auth.*

import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

@Field final http = new HTTPBuilder( 'https://api.taiga.io' )

if ( System.properties.'http.proxyHost' ) {
    http.client.getCredentialsProvider().setCredentials(
        new AuthScope( System.properties.'http.proxyHost', System.properties.'http.proxyPort'.toInteger() ),
        new UsernamePasswordCredentials( System.properties.'http.proxyUser', System.properties.'http.proxyPassword' )
    )

    http.setProxy( System.properties.'http.proxyHost', System.properties.'http.proxyPort'.toInteger(), 'http')
}

return http