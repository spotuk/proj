<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="Login" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>

	<%--=========================================================================== 
Here we use a table layout.
Class page corresponds to the '.page' element in included CSS document.
===========================================================================--%>
	<table id="main-container">

		<%--=========================================================================== 
This is the HEADER, containing a top menu.
header.jspf contains all necessary functionality for it.
Just included it in this JSP document.
===========================================================================--%>

		<%-- HEADER --%>
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<%-- HEADER --%>

		<%--=========================================================================== 
This is the CONTENT, containing the main part of the page.
===========================================================================--%>
		<tr>
			<td class="content center">
				<%-- CONTENT --%> <%--=========================================================================== 
Defines the web form.
===========================================================================--%>
				<form id="login_form" action="controller" method="post">

					<%--=========================================================================== 
Hidden field. In the query it will act as command=login.
The purpose of this to define the command name, which have to be executed 
after you submit current form. 
===========================================================================--%>
					<input type="hidden" name="command" value="login" />


					<fieldset>
						<legend>Login</legend>
						<input type="text" class="form-control" placeholder="Enter Login"
							name="login" required><br />
					</fieldset>
					<br />
					<fieldset>
						<legend>Password</legend>
						<input type="password" class="form-control"
							placeholder="Enter Password" name="password" required />
					</fieldset>
					<br />


					<div class="row justify-content-md-center">
						<div style="margin: 10px 10px 10px 10px;" >
							<input type="submit" class="btn btn-primary mb-2" value="Login">
						</div>
						<div style="margin: 10px 10px 10px 10px;" >
							<input class="btn btn-secondary" value="Add new user"
								type="button" onclick="location.href='/ST4Example/registration.jsp'" />
						</div>
					</div>
					<br />
				</form> <%-- CONTENT --%>

			</td>
		</tr>

		<%@ include file="/WEB-INF/jspf/footer.jspf"%>

	</table>
</body>
</html>