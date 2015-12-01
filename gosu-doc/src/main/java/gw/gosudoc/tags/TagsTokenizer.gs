package gw.gosudoc.tags

uses gw.gosudoc.doc.GSRootDocImpl
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
  static final var BLOCK_TAG_REGEXP = "^[ ]*@([a-zA-Z]*).*"
  static final var INLINE_TAG_PATTERN = Pattern.compile(INLINE_TAG_REGEXP);
  static final var BLOCK_TAG_PATTERN = Pattern.compile(BLOCK_TAG_REGEXP);

  var _rootDoc: GSRootDocImpl
  var _fi: IAttributedFeatureInfo
  var _src : String

  construct(src : String, featureInfo: IAttributedFeatureInfo, rootDoc: GSRootDocImpl) {
    _src = src ?: ""
    _fi = featureInfo
    _rootDoc = rootDoc
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
    if(inlineTag.startsWith("{@code ")) {
      var content = inlineTag.substring(Math.min(7, inlineTag.length() - 1), inlineTag.length() - 1)
      return "<code>${content}</code>"
    } else if(inlineTag.startsWith("{@link ")) {
      var content = inlineTag.substring(Math.min(7, inlineTag.length() - 1), inlineTag.length() - 1)
      return linkToFeature(content)
    } else if(inlineTag.equals("{@docRoot}")) {
      return inlineTag // replacement is handled by the HTML generator
    } else if(inlineTag.equals("{@inheritDoc}")) {
      return handleDescriptionInheritance(_fi, inlineTag)
    } else {
      return inlineTag
    }
  }

  private function linkToFeature(content: String): String {
    var stripped = content.trim()
    var pieces = stripped.split(" ")
    var feature = pieces.first()
    var description = pieces.length > 1 ? pieces[1] : pieces.first()
    return "<a href='${parseLink(feature)}'>${description}</a>"
  }

  private function parseLink(feature: String) : String {
    var typeAndFeature = feature.split('#')
    var linkToType = linkToType(typeAndFeature.first())
    if(typeAndFeature.length > 1) {
      return "${linkToType}#${typeAndFeature[1]}"
    } else {
      return "${linkToType}"
    }
  }

  private function linkToType(typeName: String) : String {
    if(typeName.length() == 0) {
      return ""
    } else if (typeName.indexOf('.') > 0) {
      return  "{@docRoot}/${packagePath(typeName)}/${typeName}.html"
    } else {
      return  "${_fi.OwnersType.Namespace}.${typeName}.html"
    }
  }

  private function packagePath(typeName: String) : String {
    var components = typeName.split('\\.').toList()
    return components.subList(0, components.size() - 1).join("/")
  }

  function processBlockTags(str : String) : String {

    var sb = new StringBuilder()

    var lines = str.split("\n")

    var currentTag : String
    var tagBuffer : List<String>

    for (line in lines) {
      var matcher = BLOCK_TAG_PATTERN.matcher(line)
      if (matcher.matches() && validTagName(matcher.group(1))) {
        sb.append(handleBlockCurrentTag(currentTag, tagBuffer)).append("\n")
        currentTag = matcher.group(1)
        tagBuffer = {line}
      } else {
        if (currentTag == null) {
          sb.append(line).append("\n")
        } else {
          tagBuffer.add(line)
        }
      }
    }
    sb.append(handleBlockCurrentTag(currentTag, tagBuffer)).append("\n")

    return sb.toString()
  }

  private function handleBlockCurrentTag(currentTag: String, tagBuffer: List<String>) : String {
    if(currentTag == null) {
      return ""
    } else {
      if(currentTag == "see") {
        var value = blockTagValue(currentTag, tagBuffer);
        return "<dl><dt><b>See Also:</b></dt><dd>" + linkToFeature(value) + "<dd></dl>"
      } else {
        return tagBuffer.join("\n")
      }
    }
  }

  private function blockTagValue(currentTag: String, tagBuffer: List<String>) : String {
    var sb = new StringBuilder()
    var firstLine = tagBuffer.first()
    sb.append(firstLine.substring(firstLine.indexOf('@' + currentTag) + 1 + currentTag.length())).append("\n")
    var rest = tagBuffer.iterator()
    rest.next()
    while(rest.hasNext()){
      sb.append(rest.next()).append("\n")
    }
    return sb.toString()
  }

  private function validTagName(tagName: String): boolean {
    return tagName == "see"
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