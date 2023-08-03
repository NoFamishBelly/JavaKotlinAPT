package com.leon.lib_java_compiler;

import com.google.auto.service.AutoService;
import com.leon.lib_java_annotation.LeonJavaAnn;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;


@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class LeonJavaProcessor extends AbstractProcessor {

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(LeonJavaAnn.class.getCanonicalName());
        return annotations;
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> routeElements = roundEnvironment.getElementsAnnotatedWith(LeonJavaAnn.class);

        // 遍历所有被注解了@Factory的元素
        for (Element element : routeElements) {

            LeonJavaAnn leoAnn = element.getAnnotation(LeonJavaAnn.class);
            String name = leoAnn.name();
            String data = leoAnn.data();

            String className = name + "Class";

            StringBuilder builder = new StringBuilder()
                    .append("package com.leon.auto;\n\n")
                    .append("public class ")
                    .append(className)
                    .append(" {\n\n") // open class
                    .append("\tpublic String getMessage() {\n") // open method
                    .append("\t\treturn \"");

            builder.append(name).append(data).append(" !\\n");


            builder.append("\";\n") // end return
                    .append("\t}\n") // close method
                    .append("}\n"); // close class

            try {
                JavaFileObject source = mFiler.createSourceFile("com.leon.auto." + className);
                Writer writer = source.openWriter();
                writer.write(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {

            }
        }

        return true;
    }
}
