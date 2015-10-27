package gw.gosudoc.tags

uses gw.lang.reflect.IAttributedFeatureInfo
uses gw.lang.reflect.gs.IGosuMethodInfo
uses gw.lang.reflect.gs.IGosuPropertyInfo

uses java.util.regex.Pattern
uses java.lang.StringBuilder
uses java.lang.Math

// ================================================================================
//
// This class provides an implementation of as many of the Java8 javadoc tags as is feasable
// within GosuDoc.
//
// See http://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html#CHDJGIJB
//
// ================================================================================

class TagsTokenizer {

  static final var INLINE_TAG_REGEXP = "\\{@[^}]+\\}"
  static final var INLINE_TAG_PATTERN = Pattern.compile(INLINE_TAG_REGEXP);

  var _fi: IAttributedFeatureInfo
  var _src : String

  construct(src : String, featureInfo: IAttributedFeatureInfo) {
    _src = src ?: ""
    _fi = featureInfo
  }

  function processTags() : String {

    var returnStr = _src;

    if(returnStr.contains("@")) {
      if(returnStr.contains("{@")) {
        returnStr = processInlineTags(returnStr)
      }
      if(returnStr.contains("@")) {
        returnStr = processBlockTags(returnStr)
      }
    }

    return returnStr
  }

  function processInlineTags(str : String) : String {

    var matcher  = INLINE_TAG_PATTERN.matcher(str);
    var returnStr = new StringBuilder()
    var currentLocation = 0;

    while(matcher.find()) {
      var matchStart = matcher.start()
      var matchEnd = matcher.end()

      // append stuff before the match
      returnStr.append(str.substring(currentLocation, matchStart))

      // process the tag
      returnStr.append(processInlineTag(str.substring(matchStart, matchEnd)))

      // process the tag
      currentLocation = matchEnd;
    }

    returnStr.append(str.substring(currentLocation))

    return returnStr.toString()
  }

  private function processInlineTag(inlineTag: String): String {
    if(inlineTag.startsWith("{@code")) {
      var content = inlineTag.substring(Math.min(7, inlineTag.length() - 1), inlineTag.length() - 1)
      return "<code>${content}</code>"
    } else if(inlineTag.equals("{@inheritDoc}")) {
      return handleDescriptionInheritance(_fi, inlineTag)
    } else {
      return inlineTag
    }
  }

  function processBlockTags(str : String) : String {
    return str
  }

  private function handleDescriptionInheritance(fi: IAttributedFeatureInfo, description : String): String {
    if (fi typeis IGosuMethodInfo and description?.contains("{@inheritDoc}")) {
      var superCI = fi.Dfs?.SuperDfs?.MethodOrConstructorInfo
      if (superCI != null) {
        var superDesc = handleDescriptionInheritance(superCI, superCI.Description)
        if (superDesc != null) {
          return description.replace("{@inheritDoc}", superDesc)
        }
      }
      return ""
    } else if (fi typeis IGosuPropertyInfo and description?.contains("{@inheritDoc}")) {
      var superCI = fi.Dps?.GetterDfs?.SuperDfs?.MethodOrConstructorInfo
      if (superCI != null) {
        var superDesc = handleDescriptionInheritance(superCI, superCI.Description)
        if (superDesc != null) {
          return description.replace("{@inheritDoc}", superDesc)
        }
      }
      return ""
    }
    return description
  }

}