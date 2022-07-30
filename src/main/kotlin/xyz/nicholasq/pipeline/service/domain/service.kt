package xyz.nicholasq.pipeline.service.domain

import jakarta.inject.Singleton

interface CrudService<T> {
    suspend fun findById(id: String): T
    suspend fun create(dto: T): T
    suspend fun update(dto: T): T
    suspend fun delete(id: String): Boolean
    suspend fun findAll(): List<T>
}

interface PipelineService : CrudService<Pipeline>
interface PhaseService : CrudService<Phase>
interface JobService : CrudService<Job>

abstract class AbstractCrudService<T1 : Dto, T2 : Entity, T3 : PipelineServiceEvent>(
    private val crudRepository: CrudRepository<T2, Boolean>,
    private val pipelineServiceEventService: PipelineServiceEventService,
    private val entityToDtoTransformer: EntityToDtoTransformer<T2, T1>,
    private val dtoToEntityTransformer: DtoToEntityTransformer<T1, T2>,
    private val dtoToEventTransformer: DtoToEventTransformer<T3, T1>
) : CrudService<T1> {
    override suspend fun findById(id: String): T1 {
        val entity = crudRepository.findById(id)
        return entityToDtoTransformer.toDto(entity)
    }

    //TODO: add validation on each method...maybe
    override suspend fun create(dto: T1): T1 {
        val entity = dtoToEntityTransformer.toEntity(dto)
        val savedEntity = crudRepository.save(entity)
        val savedDto = entityToDtoTransformer.toDto(savedEntity)
        if (savedDto.id == null) {
            throw IllegalStateException("Id is null")
        }
        val createEvent = dtoToEventTransformer.toCreateEvent(savedDto)
        pipelineServiceEventService.create(createEvent)
        return savedDto
    }

    override suspend fun update(dto: T1): T1 {
        val entity = dtoToEntityTransformer.toEntity(dto)
        val updatedEntity = crudRepository.update(entity)
        val updatedDto = entityToDtoTransformer.toDto(updatedEntity)
        if (updatedDto.id == null) {
            throw IllegalStateException("Id is null")
        }
        val updateEvent = dtoToEventTransformer.toUpdateEvent(updatedDto)
        pipelineServiceEventService.update(updateEvent)
        return updatedDto
    }

    override suspend fun delete(id: String): Boolean {
        val entity = crudRepository.findById(id)
        val deleteResult = crudRepository.delete(id)
        val deletedDto = entityToDtoTransformer.toDto(entity)
        val deleteEvent = dtoToEventTransformer.toDeleteEvent(deletedDto)
        pipelineServiceEventService.delete(deleteEvent)
        return deleteResult
    }

    override suspend fun findAll(): List<T1> {
        val entities = crudRepository.findAll()
        return entities.map { entityToDtoTransformer.toDto(it) }
    }
}

@Singleton
open class DefaultPipelineService(
    pipelineRepository: PipelineRepository,
    pipelineServiceEventService: PipelineServiceEventService,
) : PipelineService,
    AbstractCrudService<Pipeline, PipelineEntity, PipelineEvent>(
        pipelineRepository,
        pipelineServiceEventService,
        PipelineEntityToDtoTransformer(),
        PipelineDtoToEntityTransformer(),
        PipelineDtoToEventTransformer()
    )

@Singleton
open class DefaultPhaseService(
    phaseRepository: PhaseRepository,
    pipelineServiceEventService: PipelineServiceEventService,
) : PhaseService,
    AbstractCrudService<Phase, PhaseEntity, PhaseEvent>(
        phaseRepository,
        pipelineServiceEventService,
        PhaseEntityToDtoTransformer(),
        PhaseDtoToEntityTransformer(),
        PhaseDtoToEventTransformer()
    )

@Singleton
open class DefaultJobService(
    jobRepository: JobRepository,
    pipelineServiceEventService: PipelineServiceEventService,
) : JobService,
    AbstractCrudService<Job, JobEntity, JobEvent>(
        jobRepository,
        pipelineServiceEventService,
        JobEntityToDtoTransformer(),
        JobDtoToEntityTransformer(),
        JobDtoToEventTransformer()
    )
