package org.example.model

/**
 * Абстрактный класс для представления элемента файловой системы.
 */
abstract class FileSystemNode (val name: String) {

    /**
     * Папка-родитель элемента.
     */
    internal var parent: Directory? = null

    /**
     *
     */
    abstract fun getSize(): Int

    fun getPath(): String {
        return if (parent == null) {
            "/$name"
        } else {
            "${parent!!.getPath()}/$name"
        }
    }
}