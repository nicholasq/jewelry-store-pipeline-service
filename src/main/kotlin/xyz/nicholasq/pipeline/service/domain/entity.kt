package xyz.nicholasq.pipeline.service.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

//todo: consider adding pipelineId to this, it could be used when publishing events. so pipeline events would always
// have a pipelineId.
interface Entity {
    var id: ObjectId?
}

@Introspected
data class JobEntity @Creator @BsonCreator constructor(
    @field:BsonId @param:BsonId override var id: ObjectId?,
    @field:BsonProperty("phaseId") @param:BsonProperty("phaseId") var phaseId: String,
    @field:BsonProperty("name") @param:BsonProperty("name") var name: String,
    @field:BsonProperty("description") @param:BsonProperty("description") var description: String?,
    @field:BsonProperty("contactId") @param:BsonProperty("contactId") var contactId: String?,
    @field:BsonProperty("secondaryContactIds") @param:BsonProperty("secondaryContactIds") var secondaryContactIds: List<String>?,
) : Entity

@Introspected
data class PhaseEntity @Creator @BsonCreator constructor(
    @field:BsonId @param:BsonId override var id: ObjectId?,
    @field:BsonProperty("name") @param:BsonProperty("name") var name: String,
) : Entity

@Introspected
data class PipelineEntity @Creator @BsonCreator constructor(
    @field:BsonId @param:BsonId override var id: ObjectId?,
    @field:BsonProperty("name") @param:BsonProperty("name") var name: String,
) : Entity
