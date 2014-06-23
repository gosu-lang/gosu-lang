package gw.internal.gosu.parser.annotation.gwtest.annotation

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.UsageModifier
uses gw.lang.annotation.IInherited

@AnnotationUsage(UsageTarget.PropertyTarget, UsageModifier.One)
class AnnotationUsageTestOneOnProperty implements IAnnotation, IInherited {
}
