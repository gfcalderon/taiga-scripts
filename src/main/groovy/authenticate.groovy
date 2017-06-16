@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1')
import groovyx.net.http.HTTPBuilder
import groovy.json.JsonOutput

import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

// get login information
def user = System.console().readLine( 'Username: ' )
def pwd  = System.console().readPassword( 'Password: ' ).toString()

def http = evaluate( new File( 'getHTTPBuilder.groovy' ) )

print "Authenticating..."
http.request( POST, JSON ) { req ->
    uri.path = '/api/v1/auth'
    body = [ type: 'normal', username: user, password: pwd ]

    response.success = { resp, json ->
        println " Done!"
        return JsonOutput.toJson( [ userId: json.id, authToken: json.auth_token ] )
    }

    response.failure = { resp ->
        println "Authentication fail: ${resp.statusLine}"
        return null
    }
}
