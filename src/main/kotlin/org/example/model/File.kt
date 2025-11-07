package org.example.model

class File(name: String, private var content: String) : FileSystemNode(name) {
    override fun getSize(): Int {
        return content.length
    }

    fun read(): String {
        return content
    }

    fun write(newContent: String) {
        content = newContent
    }
}
