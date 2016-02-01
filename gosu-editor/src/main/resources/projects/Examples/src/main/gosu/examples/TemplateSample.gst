<%@ params( names: List<String> ) %>

The first name from the list of names: ${names?[0]}

All names:
<% for( name in names ) { %>
  - ${name}
<% } %>