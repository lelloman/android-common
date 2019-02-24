package com.lelloman.common.http

private const val TYPE_APPLIACTION = "application"
private const val TYPE_AUDIO = "audio"
private const val TYPE_IMAGE = "image"
private const val TYPE_MESSAGE = "message"
private const val TYPE_MODEL = "model"
private const val TYPE_MULTIPART = "multipart"
private const val TYPE_TEXT = "text"
private const val TYPE_VIDEO = "video"

sealed class ContentType(
    val subType: String
) {
    companion object {

        fun fromHeader(headerValue: String?): ContentType {
            val splitHeader = headerValue?.split("/") ?: return UnknownContentType()
            if (splitHeader.size < 2) {
                return UnknownContentType()
            }
            val (type, subType) = if (splitHeader.size > 1) splitHeader else return UnknownContentType()

            return when (type) {
                TYPE_APPLIACTION -> ApplicationContentType(subType)
                TYPE_AUDIO -> AudioContentType(subType)
                TYPE_IMAGE -> ImageContentType(subType)
                TYPE_MESSAGE -> MessageContentType(subType)
                TYPE_MODEL -> ModelContentType(subType)
                TYPE_MULTIPART -> MultipartContentType(subType)
                TYPE_TEXT -> TextContentType(subType)
                TYPE_VIDEO -> VideoContentType(subType)
                else -> UnknownContentType()
            }
        }
    }
}

class UnknownContentType : ContentType("")

class ApplicationContentType(subType: String) : ContentType(subType)

class AudioContentType(subType: String) : ContentType(subType)

class ImageContentType(subType: String) : ContentType(subType)

class MessageContentType(subType: String) : ContentType(subType)

class ModelContentType(subType: String) : ContentType(subType)

class MultipartContentType(subType: String) : ContentType(subType)

class TextContentType(subType: String) : ContentType(subType)

class VideoContentType(subType: String) : ContentType(subType)