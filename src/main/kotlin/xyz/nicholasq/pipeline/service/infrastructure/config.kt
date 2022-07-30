package xyz.nicholasq.pipeline.service.infrastructure

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.naming.Named

// todo: split the collections into: pipeline, phase, job
@ConfigurationProperties("db")
interface MongoDbConfiguration : Named {

    val collection: String
}

@ConfigurationProperties("firestore.db")
interface FirestoreConfiguration : Named {

    val collection: String
    val url: String
}
