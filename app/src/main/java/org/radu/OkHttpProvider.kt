package org.radu

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.polygon.kotlin.sdk.HttpClientProvider
import kotlinx.serialization.json.Json


open class OkHttpProvider
@JvmOverloads
constructor() : HttpClientProvider {

    open fun buildEngine(): HttpClientEngine = OkHttp.create()

    override fun buildClient(): HttpClient {
        System.out.println("Created client");
        return  HttpClient(OkHttp) {
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }
//            install(ContentEncoding)
            engine {
                threadsCount = 1000
                clientCacheSize = 1000
            }
        }
    }

    override fun getDefaultRestURLBuilder() =
            URLBuilder(
                    protocol = URLProtocol.HTTPS,
                    port = DEFAULT_PORT
            )
}