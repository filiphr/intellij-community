package de.plushnikov.intellij.plugin.processor.clazz.builder;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import de.plushnikov.intellij.plugin.problem.ProblemBuilder;
import de.plushnikov.intellij.plugin.processor.clazz.AbstractClassProcessor;
import de.plushnikov.intellij.plugin.processor.handler.BuilderHandler;
import de.plushnikov.intellij.plugin.settings.ProjectSettings;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Inspect and validate @Builder lombok annotation on a class
 * Creates inner class for a builder pattern
 *
 * @author Tomasz Kalkosiński
 * @author Michail Plushnikov
 */
public class BuilderClassProcessor extends AbstractClassProcessor {

  private final BuilderHandler builderHandler = new BuilderHandler();

  public BuilderClassProcessor() {
    this(Builder.class);
  }

  protected BuilderClassProcessor(@NotNull Class<? extends Annotation> builderClass) {
    super(builderClass, PsiClass.class);
  }

  @Override
  public boolean isEnabled(@NotNull Project project) {
    return ProjectSettings.isEnabled(project, ProjectSettings.IS_BUILDER_ENABLED);
  }

  @Override
  protected boolean validate(@NotNull PsiAnnotation psiAnnotation, @NotNull PsiClass psiClass, @NotNull ProblemBuilder builder) {
    return builderHandler.validate(psiClass, psiAnnotation, builder);
  }

  protected void generatePsiElements(@NotNull PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation, @NotNull List<? super PsiElement> target) {
    if (builderHandler.existInnerClass(psiClass, psiAnnotation)) {
      target.add(builderHandler.createBuilderClass(psiClass, psiAnnotation));
    }
  }
}
