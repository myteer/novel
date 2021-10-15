package org.myteer.novel.utils

import com.github.rholder.retry.*
import org.slf4j.LoggerFactory
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

object RetryUtils {
    private val logger = LoggerFactory.getLogger(RetryUtils::class.java)

    /** 默认尝试执行次数 */
    private const val DEFAULT_ATTEMPT_NUMBER = 3

    /** 默认尝试间隔时间(毫秒) */
    private const val DEFAULT_ATTEMPT_BETWEEN_MILLIS = 500L

    fun <T> executeWithRetryStrategy(
        attemptNumber: Int = DEFAULT_ATTEMPT_NUMBER,
        attemptBetweenMillis: Long = DEFAULT_ATTEMPT_BETWEEN_MILLIS,
        callable: Callable<T>
    ): T {
        return RetryerBuilder.newBuilder<T>()
            .retryIfException() // 发生异常时重试
            .withWaitStrategy(WaitStrategies.fixedWait(attemptBetweenMillis, TimeUnit.MILLISECONDS)) // 尝试间隔时间
            .withStopStrategy(StopStrategies.stopAfterAttempt(attemptNumber)) // 尝试次数
            .withRetryListener(object : RetryListener {
                override fun <V : Any?> onRetry(attempt: Attempt<V>) {
                    if (attempt.hasException()) {
                        logger.warn("the {} time execute failed", attempt.attemptNumber, attempt.exceptionCause);
                    }
                }
            })
            .build()
            .call(callable)
    }
}

fun <T> executeWithRetryStrategy(callable: Callable<T>): T = RetryUtils.executeWithRetryStrategy(callable = callable)

fun <T> executeWithRetryStrategy(attemptNumber: Int, attemptBetweenMillis: Long, callable: Callable<T>): T = RetryUtils.executeWithRetryStrategy(attemptNumber, attemptBetweenMillis, callable)