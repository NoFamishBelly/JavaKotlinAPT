package com.leon.leon_annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class LeonKotlinAnn(
    val name: String,
    val data: String = "默认数据"
)

