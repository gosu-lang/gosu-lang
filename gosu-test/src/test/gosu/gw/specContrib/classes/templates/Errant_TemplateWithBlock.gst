<%@ params( args : java.lang.Iterable<String> ) %>
<%
  for( arg in args.toCollection().orderBy( \ s -> s ) ) {
%>
 ${arg.charAt(0)} hi
<%
  }
%>
