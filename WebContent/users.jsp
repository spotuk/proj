<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="users" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>



<body>
	<%-- HEADER --%>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<%-- HEADER --%>

	<c:choose>
		<c:when test="${userRole.name == 'admin'}">
			<table class="table table-dark">
				<thead>
					<tr>
						<th scope="col">Login</th>
						<th scope="col">Password</th>
						<th scope="col">First Name</th>
						<th scope="col">Last Name</th>
						<th scope="col">Email</th>
						<th scope="col">Status</th>
						<th scope="col">Role</th>
						<th scope="col">Date</th>
						<th scope="col">Action</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach items="${usersList}" var="user">

						<tr>
							<td>${user.login}</td>
							<td>${user.password}</td>
							<td>${user.firstName}</td>
							<td>${user.lastName}</td>
							<td>${user.email}</td>
							<td>${user.status}</td>
							<td>${user.role}</td>
							<td>${user.date}</td>
							<td><form name="deleteUser" method="POST"
									action="controller">
									<input type="hidden" name="userId" value="${user.id}" />  <input
										type="submit" class="btn btn-danger" name="deleteUser"
										value="Delete"> <input type="hidden"
										name="command" value="cabinet" /> <input type="hidden"
										name="action" value="deleteUser" />
								</form> <c:choose>
									<c:when test="${user.status == 'active'}">
									<form name="updateUser1" method="POST"
									action="controller">
									<input type="hidden" name="userId" value="${user.id}" /> 
									<input type="hidden" name="statusId" value="2" /> 
									 <input
										type="submit" class="btn btn-outline-success" name="updateUser1"
										value="Block"> <input type="hidden"
										name="command" value="cabinet" /> <input type="hidden"
										name="action" value="updateUser" />
								</form> 
									
									
									</c:when>
									<c:when test="${user.status == 'blocked'}">
									<form name="updateUser2" method="POST"
									action="controller">
									<input type="hidden" name="userId" value="${user.id}" /> 
									<input type="hidden" name="statusId" value="1" /> 
									 <input
										type="submit" class="btn btn-outline-success" name="updateUser2"
										value="Activate"> <input type="hidden"
										name="command" value="cabinet" /> <input type="hidden"
										name="action" value="updateUser" />
								</form> 
									
									
																		</c:when>
								</c:choose></td>
						</tr>

					</c:forEach>
				</tbody>
			</table>
		</c:when>
	</c:choose>






	<!--Section: Products v.3-->

	<!--Pagination-->

	<!--Pagination-->



</body>
</html>