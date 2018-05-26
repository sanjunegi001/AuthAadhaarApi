

<script src="<c:url value="js/jquery.autocomplete.min.js" />"></script>
<link href="<c:url value="css/main.css" />" rel="stylesheet">
  <script>
 
  $(document).ready(function() {
	  
	$('#searchdocument').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/searchDoc',
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
  
        <ul class="nav navbar-nav">
         
             <li><a href="<c:url value="/esign"/>">HOME</a></li>           
              <li><a href="<c:url value="/document?searchdocument="/>">ESIGNED DOCUMENTS</a></li>
             
             
              <li>&nbsp;&nbsp;&nbsp;</li>
           
            <li><a href="authclient"><span class="glyphicon glyphicon-user"></span> Add-AuthClients</a></li>
           
           <li> 
           
          <form class="navbar-form navbar-right" role="search" >
            <div class="form-group">
              <input type="text"  class="form-control" id="searchdocument" name="searchdocument" placeholder="document name">
            </div>
            <button type="submit"  class="btn btn-default">Search</button>
        </form>
      </li>
		
      
    <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         </li>
          <li><a href="#">Welcome ${principal.username}</a></li>
		<li onclick="return document.forms.logout.submit();"><a href=""><span class="glyphicon glyphicon-user">LOGOUT</span></a></li> 
   
    </ul>
    </div>
</nav>