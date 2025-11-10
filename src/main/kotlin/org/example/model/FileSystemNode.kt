package org.example.model

/**
 * Абстрактный класс для представления элемента файловой системы.
 */
abstract class FileSystemNode(val name: String) {
    /**
     * Тип операционной системы, по умолчанию LINUX.
     */
    var os: OS = OS.LINUX

    /**
     * Папка-родитель элемента.
     * Getter публичный, setter internal.
     */
    var parent: Directory? = null
        internal set

    /**
     * Размер элемента.
     */
    abstract fun getSize(): Int

    /**
     * Получение пути к элементу с учетом типа ОС.
     */
    fun getPath(): String {
        val sep = if (os == OS.WINDOWS) "\\" else "/"
        val prefix = if (os == OS.WINDOWS) "C:" else ""

        val parts = mutableListOf<String>()
        var current: FileSystemNode? = this
        while (current != null) {
            if (current.name.isNotEmpty()) {
                parts.add(current.name)
            }
            current = current.parent
        }
        parts.reverse()

        return if (parts.isEmpty()) {
            prefix + sep
        } else {
            prefix + sep + parts.joinToString(sep)
        }
    }
}
