

<script src="<c:url value="js/jquery.autocomplete.min.js" />"></script>
<link href="<c:url value="css/main.css" />" rel="stylesheet">
  <script>
 
  $(document).ready(function() {
	  
	$('#searchuser').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/searchAuthUser.html',
		paramName: "tagName",
		delimiter: ",",
	   transformResult: function(response) {
		
		return {
        	
            suggestions: $.map($.parseJSON(response), function(item) {
            	
                return { value: item.tagName, data: item.id };
            })
            
        };

            }

	 });

  });
  </script>


<nav class="navbar navbar-default" role="navigation" style="margin-top:2%;">
  <div class="container-fluid">
 
    <div class="navbar-header">
     <a class="navbar-brand" href="#"><img  src="images/logo.jpg"></img></a>
    </div>
     <form action="./logout" name="logout" method="post">
             <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
     </form>
  <sec:authentication var="principal" property="principal" />
        <ul class="nav navbar-nav">
         
             <li><a href="<c:url value="/esign"/>">HOME</a></li>           
              <li><a href="<c:url value="/document?searchdocument="/>">ESIGNED DOCUMENTS</a></li>
             
             
              <li>&nbsp;&nbsp;&nbsp;</li>
           
           <li><a href="authclient"><span class="glyphicon glyphicon-user"></span> Add-AuthClients</a></li>
          
          <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>
          <li><a href="#">Welcome ${principal.username}</a></li>
		<li onclick="return document.forms.logout.submit();"><a href=""><span class="glyphicon glyphicon-user">LOGOUT</span></a></li> 
     <%-- <li>  
      <form action="./logout" name="logout" method="post">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		  <li style='float: left;'  onclick="return document.forms.logout.submit();"><a href="#" ><span  onclick="return document.forms.logout.submit(); class="glyphicon glyphicon-user"></span>LOGOUT</a></li>
         
      </form>
    </li> --%>
    </ul>
    </div>
</nav>