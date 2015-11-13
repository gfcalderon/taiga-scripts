@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1')
import groovyx.net.http.HTTPBuilder
import groovy.json.JsonSlurper

import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

def setTagColors( authentication, projectId ) {
    def http = new HTTPBuilder( 'https://api.taiga.io' )
    http.request( PATCH, JSON ) { req ->
        uri.path = "/api/v1/projects/${projectId}"
        headers.'Authorization' = "Bearer ${authentication.authToken}"
        body = [ tags_colors: '''{
                    "must": "#FF0000",
                    "should": "#FFBF00",
                    "could": "#00FF00",
                    "won\'t": "#999999"
                 }''' ]

        response.success = { resp, json ->
            println " Done!"
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

// invoke getMyProjects script
new GroovyShell().parse( new File( 'getMyProjects.groovy' ) ).with {
    def projects = jsonSlurper.parseText( getMyProjects( authentication ) )
    projects.each {
        print "Processing ${it.name}..."
        setTagColors( authentication, it.id )
    }
}