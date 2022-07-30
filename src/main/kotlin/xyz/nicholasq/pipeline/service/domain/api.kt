package xyz.nicholasq.pipeline.service.domain

import io.micronaut.http.annotation.*
import org.slf4j.LoggerFactory

//todo: add validation
interface CrudController<T> {
    @Post
    suspend fun create(@Body dto: T): T

    @Get("/{id}")
    suspend fun findById(@PathVariable id: String): T

    @Get
    suspend fun findAll(): List<T>

    //todo: research if id is really needed, since the dto will have it
    @Put("/{id}")
    suspend fun update(@PathVariable id: String, @Body dto: T): T

    @Delete("/{id}")
    suspend fun delete(@PathVariable id: String)
}

abstract class AbstractCrudController<T : Dto>(
    private val crudService: CrudService<T>,
    clazz: Class<*>
) : CrudController<T> {

    private val log = LoggerFactory.getLogger(clazz)

    override suspend fun create(dto: T): T {
        log.debug("create() - dto: $dto")
        return crudService.create(dto)
    }

    override suspend fun findById(id: String): T {
        log.debug("findById() - id: $id")
        return crudService.findById(id)
    }

    override suspend fun findAll(): List<T> {
        log.debug("findAll()")
        return crudService.findAll()
    }

    override suspend fun update(id: String, dto: T): T {
        log.debug("update() - id: $id, dto: $dto")
        return crudService.update(dto)
    }

    override suspend fun delete(id: String) {
        log.debug("delete() - id: $id")
        crudService.delete(id)
    }
}

@Controller("/api/v1/pipeline")
open class PipelineController(pipelineService: PipelineService) :
    AbstractCrudController<Pipeline>(
        pipelineService,
        PipelineController::class.java
    )

@Controller("/api/v1/phase")
open class PhaseController(phaseService: PhaseService) :
    AbstractCrudController<Phase>(
        phaseService,
        PhaseController::class.java
    )


@Controller("/api/v1/job")
open class JobController(jobService: JobService) :
    AbstractCrudController<Job>(
        jobService,
        JobController::class.java
    )
