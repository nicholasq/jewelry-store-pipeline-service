package xyz.nicholasq.pipeline.service.domain

import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import kotlinx.coroutines.reactive.awaitFirst
import org.bson.BsonObjectId
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.reactivestreams.findOne
import xyz.nicholasq.pipeline.service.infrastructure.MongoDbConfiguration


interface CrudRepository<T, D> {
    suspend fun save(entity: T): T
    suspend fun update(entity: T): T
    suspend fun findById(id: String): T
    suspend fun findAll(): List<T>
    suspend fun delete(id: String): D
}

interface PipelineRepository : CrudRepository<PipelineEntity, Boolean> {}

interface PhaseRepository : CrudRepository<PhaseEntity, Boolean> {}

interface JobRepository : CrudRepository<JobEntity, Boolean> {}

abstract class AbstractMongoRepository<T : Entity>(
    mongoConf: MongoDbConfiguration,
    mongoClient: MongoClient,
    clazz: Class<T>
) : CrudRepository<T, Boolean> {

    private val collection: MongoCollection<T> = mongoClient.getDatabase(mongoConf.name)
        .getCollection(mongoConf.collection, clazz)

    override suspend fun save(entity: T): T {
        val insertResult = collection.insertOne(entity).awaitFirst()
        return findById((insertResult.insertedId as BsonObjectId).value.toString())
    }

    override suspend fun update(entity: T): T {
        val updated = collection.findOneAndReplace(Filters.eq("_id", entity.id), entity).awaitFirst()
        return findById(updated.id.toString())
    }

    override suspend fun findById(id: String): T {
        return collection.findOne(Filters.eq("_id", ObjectId(id))).awaitFirst()
    }

    override suspend fun findAll(): List<T> {
        return collection.find().toList()
    }

    override suspend fun delete(id: String): Boolean {
        val deletedCount = collection.deleteOne(Filters.eq("_id", ObjectId(id))).awaitFirst().deletedCount
        return deletedCount > 0L
    }
}

@Singleton
open class MongoPipelineRepository(mongoConf: MongoDbConfiguration, mongoClient: MongoClient) :
    AbstractMongoRepository<PipelineEntity>(mongoConf, mongoClient, PipelineEntity::class.java), PipelineRepository

@Singleton
open class MongoPhaseRepository(mongoConf: MongoDbConfiguration, mongoClient: MongoClient) :
    AbstractMongoRepository<PhaseEntity>(mongoConf, mongoClient, PhaseEntity::class.java), PhaseRepository

@Singleton
open class MongoJobRepository(mongoConf: MongoDbConfiguration, mongoClient: MongoClient) :
    AbstractMongoRepository<JobEntity>(mongoConf, mongoClient, JobEntity::class.java), JobRepository
