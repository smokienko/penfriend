package com.smokiyenko.penfriend.processor;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.smokiyenko.penfriend.annotation.PenFriend;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class DynamicServiceProcessor extends AbstractProcessor {

    private static final String PREFIX = "Proxy";

    private Elements elementUtils;
    private Messager messager;
    private Filer filer;

    private ClassName contextClass =  ClassName.get("android.content", "Context");
    private ClassName intentClass =  ClassName.get("android.content", "Intent");
    private ClassName bundler = ClassName.get("com.smokiyenko.android.dynamicservice.bundlers","BundleBuilder");

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.messager = processingEnvironment.getMessager();
        this.elementUtils = processingEnvironment.getElementUtils();
        this.filer = processingEnvironment.getFiler();
    }


        @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        List<ServiceProxyDescriptor> descriptors = new ArrayList<>();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(PenFriend.class)) {
            if (!element.getKind().isClass()) {
                messager.printMessage(Diagnostic.Kind.ERROR, "PenFriend can be applied only to Class");
                return true;
            }

            ServiceProxyDescriptor proxyDescriptor = new ServiceProxyDescriptor();
            proxyDescriptor.setClassName(interfaceName(element));
            proxyDescriptor.setPackageName(elementUtils.getPackageOf(element).getQualifiedName().toString());
            proxyDescriptor.setParent(element.asType());
            List<MethodDescriptor> methods = new ArrayList<>();
            for (Element classElement : element.getEnclosedElements()) {
                if (isDynamicApi(classElement)) {
                    MethodDescriptor method = new MethodDescriptor();
                    method.setMethodName(classElement.getSimpleName().toString());
                    List<ParameterDescriptor> parameters = new ArrayList<>();
                    for (VariableElement parameterElement : ((ExecutableElement) classElement).getParameters()) {
                        ParameterDescriptor param = new ParameterDescriptor();
                        param.setName(parameterElement.getSimpleName().toString());
                        param.setType(parameterElement.asType());
                        parameters.add(param);
                    }
                    method.setParameters(parameters);
                    methods.add(method);
                }
            }
            proxyDescriptor.setMethods(methods);
            descriptors.add(proxyDescriptor);
        }

            generateClasses(descriptors);
        return true;
    }

    private String interfaceName(Element element){
        return String.format("%s%s", element.getSimpleName().toString(), PREFIX);
    }


    private boolean isDynamicApi(Element element) {
        messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "isDynamicApi"
                + " name " + element.getSimpleName().toString()
                + " isPublic " + element.getModifiers().contains(Modifier.PUBLIC)
                + " isStatic " + !element.getModifiers().contains(Modifier.STATIC)
        );
        return element instanceof ExecutableElement
                && !element.getSimpleName().toString().startsWith("<")
                && element.getModifiers().contains(Modifier.PUBLIC)
                && !element.getModifiers().contains(Modifier.STATIC);
    }

    private void generateClasses(List<ServiceProxyDescriptor> descriptors) {
        for (ServiceProxyDescriptor descriptor : descriptors) {
            messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "Class generation ");
            TypeSpec.Builder proxyBuilder = TypeSpec
                    .classBuilder(descriptor.getClassName())
                    .addField(contextClass, "context", Modifier.PRIVATE)
                    .addField(Class.class, "penFriendType", Modifier.PRIVATE)
                    .superclass(TypeName.get(descriptor.getParent()))
                    .addModifiers(Modifier.PUBLIC);

            MethodSpec constructor = MethodSpec
                    .constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(contextClass, "context")
                    .addParameter(Class.class, "penFriendType")
                    .addStatement("this.context = context")
                    .addStatement("this.penFriendType = penFriendType")
                    .build();

            proxyBuilder.addMethod(constructor);

            for (MethodDescriptor methodDescriptor : descriptor.getMethods()) {
                messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "Method generation " + methodDescriptor.getMethodName());
                MethodSpec.Builder proxyMethod = MethodSpec
                        .methodBuilder(methodDescriptor.getMethodName())
                        .addAnnotation(Override.class);
                for (ParameterDescriptor parameterDescriptor : methodDescriptor.getParameters()) {
                    messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "Params generation " + parameterDescriptor.getName());
                    ParameterSpec spec = ParameterSpec
                            .builder(TypeName.get(parameterDescriptor.getType()), parameterDescriptor.getName())
                            .build();
                    proxyMethod.addParameter(spec);
                }
                proxyMethod.addStatement("$T startIntent = new $T(context, penFriendType)", intentClass, intentClass);
                proxyMethod.addStatement("$T.buildMethodDescriptionBundle(startIntent, $S, $T.class) ", bundler, methodDescriptor.getMethodName(), descriptor.getParent());
                for (ParameterDescriptor parameterDescriptor : methodDescriptor.getParameters()) {
                    if (parameterDescriptor.getType().getKind().isPrimitive()) {
                        proxyMethod.addStatement("$T.addPrimitiveMethodArg(startIntent, $T.class , $N)",bundler, parameterDescriptor.getType(), parameterDescriptor.getName());
                    } else {
                        proxyMethod.addStatement("$T.addMethodArg(startIntent, $N)",bundler, parameterDescriptor.getName());
                    }
                }
                proxyMethod.addStatement("context.startService(startIntent)");
                proxyBuilder.addMethod(proxyMethod.addModifiers(Modifier.PUBLIC).build());
                messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "Method added " + methodDescriptor.getMethodName());
            }


            try {
                JavaFile.builder(descriptor.getPackageName(), proxyBuilder.build()).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(PenFriend.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
