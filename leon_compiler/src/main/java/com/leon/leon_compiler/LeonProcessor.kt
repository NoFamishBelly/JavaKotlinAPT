package com.leon.leon_compiler

import com.google.auto.service.AutoService
import com.leon.leon_annotation.LeonAnn
import java.io.Writer
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
class LeonProcessor : AbstractProcessor() {

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
        annotations.add(LeonAnn::class.java.canonicalName)
        return annotations
    }


    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {

        p1?.run {
            val leonAnnElements = getElementsAnnotatedWith(LeonAnn::class.java)
            //遍历所有被注解了LeonAnn的元素
            leonAnnElements.forEach { e ->
                val leonAnn = e.getAnnotation(LeonAnn::class.java) as LeonAnn
                val name = leonAnn.name
                val data = leonAnn.data

                val className = name + "Class"
                val builder = StringBuilder()
                    .append("package com.leon.ann;\n\n")
                    .append("public class ")
                    .append(className)
                    .append(" {\n\n") // open class
                    .append("\tpublic String getMessage() {\n") // open method
                    .append("\t\treturn \"")

                builder.append(data).append(" !\\n")

                builder.append("\";\n") // end return
                    .append("\t}\n") // close method
                    .append("}\n") // close class

                var writer: Writer? = null
                try {
                    val source = mFiler.createSourceFile("com.leon.ann.$className")
                    writer = source.openWriter()
                    writer.write(builder.toString())
                    writer.flush()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        writer?.run {
                            close()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }





        return true
    }
}