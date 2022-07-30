package xyz.nicholasq.pipeline.service.domain

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

interface PipelineServiceEventService {
    fun create(pipelineServiceEvent: PipelineServiceEvent)
    fun update(pipelineServiceEvent: PipelineServiceEvent)
    fun delete(pipelineServiceEvent: PipelineServiceEvent)
}

@Singleton
open class PubsubPipelineServiceEventService : PipelineServiceEventService {

    private val log = LoggerFactory.getLogger(PubsubPipelineServiceEventService::class.java)

    override fun create(pipelineServiceEvent: PipelineServiceEvent) {
        log.debug("create: {}", pipelineServiceEvent)
    }

    override fun update(pipelineServiceEvent: PipelineServiceEvent) {
        log.debug("update: {}", pipelineServiceEvent)
    }

    override fun delete(pipelineServiceEvent: PipelineServiceEvent) {
        log.debug("delete: {}", pipelineServiceEvent)
    }
}

// consider adding pipelineId
interface PipelineServiceEvent {
    val id: String
    val eventType: EventType
    val eventDate: LocalDateTime
    val serviceType: ServiceType
}

enum class EventType {
    CREATE, UPDATE, DELETE
}

enum class ServiceType {
    PIPELINE, PHASE, JOB
}

data class PipelineEvent(
    override val id: String,
    override val eventType: EventType,
    override val eventDate: LocalDateTime,
    val pipelineId: String,
    override val serviceType: ServiceType = ServiceType.PIPELINE,
) : PipelineServiceEvent

data class PhaseEvent(
    override val id: String,
    override val eventType: EventType,
    override val eventDate: LocalDateTime,
    val phaseId: String,
    override val serviceType: ServiceType = ServiceType.PHASE,
) : PipelineServiceEvent

data class JobEvent(
    override val id: String,
    override val eventType: EventType,
    override val eventDate: LocalDateTime,
    val jobId: String,
    override val serviceType: ServiceType = ServiceType.JOB,
) : PipelineServiceEvent
