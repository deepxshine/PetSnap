package com.example.petsnap.domain.model

data class PageResponse<T>(
    val content: List<T>,
    val pageable: Pageable,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Long,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val first: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)

data class Pageable(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: Sort,
    val offset: Long,
    val paged: Boolean,
    val unpaged: Boolean
)

data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)
