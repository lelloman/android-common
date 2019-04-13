package com.lelloman.common.http

private const val TYPE_APPLICATION = "application"
private const val TYPE_AUDIO = "audio"
private const val TYPE_IMAGE = "image"
private const val TYPE_MESSAGE = "message"
private const val TYPE_MODEL = "model"
private const val TYPE_MULTIPART = "multipart"
private const val TYPE_TEXT = "text"
private const val TYPE_VIDEO = "video"

private const val SUB_TYPE_HTML = "html"
private const val SUB_TYPE_PLAIN = "plain"

sealed class ContentType(
    val type: String,
    val subType: String
) {
    companion object {

        fun fromHeader(headerValue: String?): ContentType {
            val splitHeader = headerValue?.split("/") ?: return UnknownContentType()
            if (splitHeader.size < 2) {
                return UnknownContentType()
            }
            val (type, subType) = if (splitHeader.size > 1) {
                splitHeader[0] to splitHeader[1].split(";")[0]
            } else return UnknownContentType()

            return when (type) {
                TYPE_APPLICATION -> ApplicationContentType(subType)
                TYPE_AUDIO -> AudioContentType(subType)
                TYPE_IMAGE -> ImageContentType(subType)
                TYPE_MESSAGE -> MessageContentType(subType)
                TYPE_MODEL -> ModelContentType(subType)
                TYPE_MULTIPART -> MultipartContentType(subType)
                TYPE_TEXT -> when (subType) {
                    SUB_TYPE_HTML -> HtmlTextContentType
                    SUB_TYPE_PLAIN -> PlainTextContentType
                    else -> UnknownTextContentType(subType)
                }
                TYPE_VIDEO -> VideoContentType(subType)
                else -> UnknownContentType(type, subType)
            }
        }
    }
}

class UnknownContentType(type: String = "", subType: String = "") : ContentType(type, subType)

class ApplicationContentType(subType: String) : ContentType(TYPE_APPLICATION, subType)

class AudioContentType(subType: String) : ContentType(TYPE_AUDIO, subType)

class ImageContentType(subType: String) : ContentType(TYPE_IMAGE, subType)

class MessageContentType(subType: String) : ContentType(TYPE_MESSAGE, subType)

class ModelContentType(subType: String) : ContentType(TYPE_MODEL, subType)

class MultipartContentType(subType: String) : ContentType(TYPE_MULTIPART, subType)

class VideoContentType(subType: String) : ContentType(TYPE_VIDEO, subType)


sealed class TextContentType(subType: String) : ContentType(TYPE_TEXT, subType)

class UnknownTextContentType(subType: String) : TextContentType(subType)

object PlainTextContentType : TextContentType(SUB_TYPE_PLAIN)

object HtmlTextContentType : TextContentType(SUB_TYPE_HTML)
