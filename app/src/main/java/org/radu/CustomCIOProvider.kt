package org.radu

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.polygon.kotlin.sdk.HttpClientProvider
import kotlinx.serialization.json.Json


open class CustomCIOProvider
@JvmOverloads
constructor() : HttpClientProvider {

    override fun buildClient(): HttpClient {
        return  HttpClient(CIO) {
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }
            engine {
                maxConnectionsCount = 1_000
                requestTimeout = 10_000

                endpoint {
                    connectTimeout = 10_000
                    requestTimeout = 120_000
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