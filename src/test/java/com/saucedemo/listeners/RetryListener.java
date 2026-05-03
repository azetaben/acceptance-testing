package com.saucedemo.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Wires RetryAnalyzer onto every @Test method automatically.
 * Register in testng XML:
 *   &lt;listeners&gt;
 *     &lt;listener class-name="com.saucedemo.listeners.RetryListener"/&gt;
 *   &lt;/listeners&gt;
 */
public class RetryListener implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        if (annotation.getRetryAnalyzerClass() == null) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}