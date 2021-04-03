package org.radu

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.polygon.kotlin.sdk.HttpClientProvider
import kotlinx.serialization.json.Json


open class CustomApacheProvider
@JvmOverloads
constructor() : HttpClientProvider {

    override fun buildClient(): HttpClient {
        return  HttpClient(Apache) {
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }
            engine {
                // this: ApacheEngineConfig
                followRedirects = true
                socketTimeout = 100_000
                connectTimeout = 10_000
                connectionRequestTimeout = 60_000
                customizeClient {
                    // this: HttpAsyncClientBuilder
                    setMaxConnTotal(1000)
                    setMaxConnPerRoute(1000)
                    // ...
                }
                customizeRequest {
                    // this: RequestConfig.Builder
                }
            }
        }
    }

    override fun getDefaultRestURLBuilder() =
            URLBuilder(
                    protocol = URLProtocol.HTTPS,
                    port = DEFAULT_PORT
            )
}