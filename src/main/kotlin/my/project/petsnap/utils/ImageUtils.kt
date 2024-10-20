package my.project.petsnap.utils

import my.project.petsnap.exception.FileEmptyException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Component
class ImageUtils (
     @Value("\${upload.path}") private val uploadPath: String,

) {

    // check if uploaded file is an  image file
    fun isImageFile(file: MultipartFile): Boolean {
        val contentType = file.contentType
        return contentType != null && contentType.startsWith("image")
    }

    // generate url from image
    fun generateUrl(file: MultipartFile): String {

        if (file.isEmpty) {
            throw FileEmptyException("File is empty")
        }

        return try {
            // if upload path doesn't exist, then create this path
            val uploadDir = Paths.get(uploadPath)
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir)
            }

            // UUID: Universally Unique Identifier -- 32 hexadecimal digits + file type
            val fileName = UUID.randomUUID().toString() + "." + file.originalFilename?.substringAfterLast(".")
            // filePath = uploadPath/filename
            val filePath = Paths.get(uploadPath, fileName)
            // copy the file from inputStream to filePath
            Files.copy(file.inputStream, filePath)

            // create a URL for imageFile
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(fileName)
                .toUriString()
        } catch (e: Exception) {
            throw RuntimeException("Failed to upload file", e)
        }
    }
}