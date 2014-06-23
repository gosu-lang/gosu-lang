package gw.internal.gosu.parser.annotation.gwtest.annotation

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.IInherited

@AnnotationUsage(gw.lang.annotation.UsageTarget.MethodTarget, gw.lang.annotation.UsageModifier.One)
class AnnotationUsageTestOneOnMethod implements IAnnotation, IInherited {
}
