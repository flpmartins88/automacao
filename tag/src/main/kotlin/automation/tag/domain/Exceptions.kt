package automation.tag.domain

import org.springframework.http.HttpStatus

open class RestException(
    val errorCode: String,
    val httpStatus: HttpStatus,
    val defaultMessage: String = "",
    params: Array<Any> = emptyArray(),
    cause: Throwable? = null
) : Exception(errorCode, cause)

class TagNotFoundException(id: Long) : RestException(
    errorCode = ErrorCode.TAG_NOT_FOUND,
    httpStatus = HttpStatus.NOT_FOUND,
    defaultMessage = "Tag ID '$id' not found",
    params = arrayOf(id)
)

class TagAlreadyProducedException(id: String) : RestException(
    errorCode = ErrorCode.TAG_ALREADY_PRODUCED,
    httpStatus = HttpStatus.BAD_REQUEST,
    defaultMessage = "Tag ID '$id' already produced",
    params = arrayOf(id)
)

object ErrorCode {
    const val TAG_NOT_FOUND = "tag.not_found"
    const val TAG_ALREADY_PRODUCED = "tag.already_produced"
}