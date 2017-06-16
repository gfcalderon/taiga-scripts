@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1')
import groovyx.net.http.HTTPBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

def getMyProjects( authentication ) {
    def http = evaluate( new File( 'getHTTPBuilder.groovy' ) )

    print "Getting projects..."
    http.request( GET, JSON ) { req ->
        uri.path = '/api/v1/projects'
        uri.query = [ member: authentication.userId ]
        headers.'Authorization' = "Bearer ${authentication.authToken}"

        response.success = { resp, json ->
            println " Done!"
            def projects = []
            json.each {
                if ( it.i_am_owner ) {
                    projects.push( [ id: it.id, name: it.name ] )
                }
            }
            return JsonOutput.toJson( projects )
        }

        response.failure = { resp ->
            println "Unexpected fail: ${resp.statusLine}"
            System.exit( -2 )
        }
    }
}

def jsonSlurper = new JsonSlurper()

// invoke authenticate script
def authentication = jsonSlurper.parseText( evaluate( new File( 'authenticate.groovy' ) ) )
if ( !authentication.authToken ) {
    System.exit( -1 )
}

def projects = jsonSlurper.parseText( getMyProjects( authentication ) )
projects.each {
    println "  ${it.name} ( ${it.id} )"
}
