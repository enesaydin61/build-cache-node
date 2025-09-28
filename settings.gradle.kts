import java.net.URI
import java.util.Optional

rootProject.name = "build-cache-node"

val remoteCacheUrl = URI(System.getProperty("build.cache.node.url", "http://localhost:5071/cache/"))

val isJenkins = Optional.ofNullable(System.getenv("WORKSPACE")).isPresent

val cacheNodeUsername: String = System.getProperty("cache.node.user", "build.cache.user")
val cacheNodePassword: String = System.getProperty("cache.node.password", "123456!")

buildCache {

    remote<HttpBuildCache> {
        isPush = true
        url = remoteCacheUrl
        isAllowUntrustedServer = true
        isAllowInsecureProtocol = true

        if (cacheNodeUsername.isNotEmpty() && cacheNodePassword.isNotEmpty()) {

            println("Gradle remote build cache ...")

            credentials {
                username = cacheNodeUsername
                password = cacheNodePassword
            }
        }
    }
}