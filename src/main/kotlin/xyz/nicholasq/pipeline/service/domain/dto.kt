package xyz.nicholasq.pipeline.service.domain

import io.micronaut.core.annotation.Introspected

//todo: consider adding pipelineId to this, it could be used when publishing events. so pipeline events would always
// have a pipelineId.
interface Dto {
    var id: String?
}

@Introspected
data class Job(
    override var id: String?,
    var phaseId: String,
    var name: String,
    var description: String?,
    var contactId: String?,
    var secondaryContactIds: List<String>?,
) : Dto

@Introspected
data class Phase(
    override var id: String?,
    var name: String,
) : Dto

@Introspected
data class Pipeline(
    override var id: String?,
    var name: String,
) : Dto
