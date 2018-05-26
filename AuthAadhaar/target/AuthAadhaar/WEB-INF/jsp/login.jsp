<%@ include file="includes/loginheader.jsp" %>
<div id="modal">
            <img id="loader" src="<c:url value="/images/logo.png"/>" />
 </div>
 <div id="login-overlay" class="modal-dialog">
      <div class="modal-content">
          <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
              <h2 class="modal-title" id="myModalLabel"><center>Login to Auth Aadhaar</center></h2>
          </div>
          <div class="modal-body">
              <div class="row">
                  <div class="col-xs-6">
                      <div class="well">
                         
                          <form:form action="./login" method="post" class="form-signin">
                              <div class="form-group">
                                  <label class="control-label">Username</label>
                                  <input type="text" class="form-control" name="username" placeholder="UserName" required autofocus>
                                  <input type="hidden" name="${_csrf.parameterName}"
						           value="${_csrf.token}" />
                                  <span class="help-block"></span>
                              </div>
                              <div class="form-group">
                                  <label  class="control-label">Password</label>
                                  <input type="password" class="form-control" name="password" placeholder="Password" required>
                                  <span class="help-block"></span>
                              </div>
                              
                              
                              <button type="submit" class="btn btn-success btn-block">Login</button>
                              </form:form>
                             <c:if test="${param.error ne null}">
								<div class="alert-danger">Invalid Username or password.</div>
							</c:if>
							<c:if test="${param.logout ne null}">
								<div class="alert-normal">You have been logged out.</div>
							</c:if>
                     
                      </div>
                  </div>
                  
                 
              </div>
          </div>
      </div>
  </div>

<%@ include file="includes/footer.jsp" %>	