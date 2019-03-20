<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="categories" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>



<body>
	<%-- HEADER --%>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<%-- HEADER --%>
	<table class="table table-dark">
		<thead>
			<tr>
				<th scope="col">Name</th>
				<th scope="col">Sort Order</th>
				<th scope="col">Action</th>
				<th scope="col"> <a class="btn btn-outline-success"
						href="controller?command=category&action=categoryDirection&type=create"
						role="button">Create new category</a></th>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${categoriesList}" var="category">

				<tr>
					<td><a class="btn btn-outline-success"
						href="controller?command=category&action=category&categoryId=${category.id}"
						role="button">${category.title}</a></td>
					<td>${category.sortOrder}</td>
					<td><form name="categoryDelete" method="POST" action="controller">
									<input type="hidden" name="categoryId" value="${category.id}" /> <input
										type="submit" class="btn btn-danger" name="categoryDelete"
										value="Delete"> <input type="hidden" name="command"
										value="category" /> <input type="hidden" name="action"
										value="categoryDelete" />
								</form>
								<form name="categoryUpdate" method="POST" action="controller">
									<input type="hidden" name="categoryId" value="${category.id}" /> <input
										type="submit" class="btn btn-danger" name="categoryUpdate"
										value="Update"> <input type="hidden" name="command"
										value="category" /> <input type="hidden" name="action"
										value="categoryDirection" />
										<input type="hidden" name="type"
										value="update" />
								</form></td>
				</tr>

			</c:forEach>
		</tbody>
	</table>



	<!--Section: Products v.3-->

	<!--Pagination-->

	<!--Pagination-->



</body>
</html>