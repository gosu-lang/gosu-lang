package gw.internal.gosu.parser.annotation.gwtest.annotation

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.IInherited

@AnnotationUsage(gw.lang.annotation.UsageTarget.ConstructorTarget, gw.lang.annotation.UsageModifier.One)
class AnnotationUsageTestOneOnConstructor implements IAnnotation, IInherited {
}
