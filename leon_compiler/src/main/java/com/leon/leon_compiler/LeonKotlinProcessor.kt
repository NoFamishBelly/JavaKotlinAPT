package com.leon.leon_compiler

import com.google.auto.service.AutoService
import com.leon.leon_annotation.LeonKotlinAnn
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
class LeonKotlinProcessor : AbstractProcessor() {

    private lateinit var mFiler: Filer


    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        processingEnv?.run {
            mFiler = filer
        }
    }

    //支持的注解类型LeonAnn
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val annotations = LinkedHashSet<String>()
        annotations.add(LeonKotlinAnn::class.java.canonicalName)
        return annotations
    }


    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {

        p1?.run {
            val leonAnnElements = getElementsAnnotatedWith(LeonKotlinAnn::class.java)
            //遍历所有被注解了LeonAnn的元素
            leonAnnElements.forEach { e ->
                val leonAnn = e.getAnnotation(LeonKotlinAnn::class.java) as LeonKotlinAnn
                val name = leonAnn.name
                val data = leonAnn.data
                val className = name + "KotlinClass"

                val fileBuilder = FileSpec.builder("com.leon.ann", className)

                val classBuilder = TypeSpec.classBuilder(className)
                    .addFunction(
                        FunSpec.builder("getMessage")
                            .returns(String::class)
                            .addStatement("return \" $data\\n\"")
                            .build()
                    )
                    .build()

                fileBuilder.addType(classBuilder)

                val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
                fileBuilder.build().writeTo(File(kaptKotlinGeneratedDir))
            }
        }
        return true
    }
}