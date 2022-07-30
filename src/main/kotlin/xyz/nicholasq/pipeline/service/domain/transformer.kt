package xyz.nicholasq.pipeline.service.domain

import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.util.*

interface EntityToDtoTransformer<T1 : Entity, T2 : Dto> {
    fun toDto(entity: T1): T2
}

interface DtoToEntityTransformer<T1 : Dto, T2 : Entity> {
    fun toEntity(dto: T1): T2
}

interface DtoToEventTransformer<T1 : PipelineServiceEvent, T2 : Dto> {
    fun toCreateEvent(dto: T2): T1
    fun toUpdateEvent(dto: T2): T1
    fun toDeleteEvent(dto: T2): T1
}

class PipelineEntityToDtoTransformer : EntityToDtoTransformer<PipelineEntity, Pipeline> {

    override fun toDto(entity: PipelineEntity): Pipeline {
        return Pipeline(
            id = entity.id?.toString(),
            name = entity.name,
        )
    }
}

class PhaseEntityToDtoTransformer : EntityToDtoTransformer<PhaseEntity, Phase> {

    override fun toDto(entity: PhaseEntity): Phase {
        return Phase(
            id = entity.id?.toString(),
            name = entity.name,
        )
    }
}

class JobEntityToDtoTransformer : EntityToDtoTransformer<JobEntity, Job> {

    override fun toDto(entity: JobEntity): Job {
        return Job(
            id = entity.id?.toString(),
            name = entity.name,
            phaseId = entity.phaseId,
            description = entity.description,
            contactId = entity.contactId,
            secondaryContactIds = entity.secondaryContactIds
        )
    }
}

class PipelineDtoToEntityTransformer : DtoToEntityTransformer<Pipeline, PipelineEntity> {
    override fun toEntity(dto: Pipeline): PipelineEntity {
        return PipelineEntity(
            id = if (dto.id != null) ObjectId(dto.id) else null,
            name = dto.name,
        )
    }
}

class PhaseDtoToEntityTransformer : DtoToEntityTransformer<Phase, PhaseEntity> {
    override fun toEntity(dto: Phase): PhaseEntity {
        return PhaseEntity(
            id = if (dto.id != null) ObjectId(dto.id) else null,
            name = dto.name,
        )
    }
}

class JobDtoToEntityTransformer : DtoToEntityTransformer<Job, JobEntity> {
    override fun toEntity(dto: Job): JobEntity {
        return JobEntity(
            id = if (dto.id != null) ObjectId(dto.id) else null,
            name = dto.name,
            phaseId = dto.phaseId,
            description = dto.description,
            contactId = dto.contactId,
            secondaryContactIds = dto.secondaryContactIds
        )
    }
}

fun toPipelineEvent(dto: Pipeline, eventType: EventType, serviceType: ServiceType): PipelineEvent {
    return PipelineEvent(
        id = UUID.randomUUID().toString(),
        eventType = eventType,
        eventDate = LocalDateTime.now(),
        pipelineId = dto.id!!,
        serviceType = serviceType,
    )
}

//todo: validate dto.id so we know it's safe to cast
class PipelineDtoToEventTransformer : DtoToEventTransformer<PipelineEvent, Pipeline> {
    override fun toCreateEvent(dto: Pipeline): PipelineEvent {
        return toPipelineEvent(
            dto = dto,
            eventType = EventType.CREATE,
            serviceType = ServiceType.PIPELINE,
        )
    }

    override fun toUpdateEvent(dto: Pipeline): PipelineEvent {
        return toPipelineEvent(
            dto = dto,
            eventType = EventType.UPDATE,
            serviceType = ServiceType.PIPELINE,
        )
    }

    override fun toDeleteEvent(dto: Pipeline): PipelineEvent {
        return toPipelineEvent(
            dto = dto,
            eventType = EventType.DELETE,
            serviceType = ServiceType.PIPELINE,
        )
    }
}

fun toPhaseEvent(dto: Phase, eventType: EventType, serviceType: ServiceType): PhaseEvent {
    return PhaseEvent(
        id = UUID.randomUUID().toString(),
        eventType = eventType,
        eventDate = LocalDateTime.now(),
        phaseId = dto.id!!,
        serviceType = serviceType,
    )
}

class PhaseDtoToEventTransformer : DtoToEventTransformer<PhaseEvent, Phase> {
    override fun toCreateEvent(dto: Phase): PhaseEvent {
        return toPhaseEvent(
            dto = dto,
            eventType = EventType.CREATE,
            serviceType = ServiceType.PHASE,
        )
    }

    override fun toUpdateEvent(dto: Phase): PhaseEvent {
        return toPhaseEvent(
            dto = dto,
            eventType = EventType.UPDATE,
            serviceType = ServiceType.PHASE,
        )
    }

    override fun toDeleteEvent(dto: Phase): PhaseEvent {
        return toPhaseEvent(
            dto = dto,
            eventType = EventType.DELETE,
            serviceType = ServiceType.PHASE,
        )
    }
}

fun toJobEvent(dto: Job, eventType: EventType, serviceType: ServiceType): JobEvent {
    return JobEvent(
        id = UUID.randomUUID().toString(),
        eventType = eventType,
        eventDate = LocalDateTime.now(),
        jobId = dto.id!!,
        serviceType = serviceType,
    )
}

class JobDtoToEventTransformer : DtoToEventTransformer<JobEvent, Job> {
    override fun toCreateEvent(dto: Job): JobEvent {
        return toJobEvent(
            dto = dto,
            eventType = EventType.CREATE,
            serviceType = ServiceType.JOB,
        )
    }

    override fun toUpdateEvent(dto: Job): JobEvent {
        return toJobEvent(
            dto = dto,
            eventType = EventType.UPDATE,
            serviceType = ServiceType.JOB,
        )
    }

    override fun toDeleteEvent(dto: Job): JobEvent {
        return toJobEvent(
            dto = dto,
            eventType = EventType.DELETE,
            serviceType = ServiceType.JOB,
        )
    }
}
