package org.example.model

interface Searchable {
    fun find(name: String): FileSystemNode?
}