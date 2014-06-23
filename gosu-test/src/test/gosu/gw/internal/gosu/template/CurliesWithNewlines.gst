<%@ params(_city : String, _state : String, _today : java.util.Date) %>

City : ${_city}
State : ${_state}

Generated on <%= _today %>

<% if(_today.before( null as java.util.Date )) { %>
Generated before 11/01/08
<% } %>