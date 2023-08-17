package com.ke.music.domain

import com.ke.music.common.domain.IUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal abstract class UseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    IUseCase<P, R> {

    /** Executes the use case asynchronously and returns a [Result].
     *
     * @return a [Result].
     *
     * @param parameters the input parameters to run the use case with
     */
    override suspend operator fun invoke(parameters: P): com.ke.music.common.domain.Result<R> {
        return try {
            // Moving all use case's executions to the injected dispatcher
            // In production code, this is usually the Default dispatcher (background thread)
            // In tests, this becomes a TestCoroutineDispatcher
            withContext(coroutineDispatcher) {
                execute(parameters).let {
                    com.ke.music.common.domain.Result.Success(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            com.ke.music.common.domain.Result.Error(e)
        }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}
