<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="products" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>



<body>
	<%-- HEADER --%>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<%-- HEADER --%>

	<c:choose>
		<c:when test="${userRole.name == 'admin'}">
			<table class="table table-dark">


				<a class="btn btn-outline-danger"
					href="controller?command=product&action=productDirect&type=create"
					role="button">Create New Product</a>
				<thead>
					<tr>
						<th scope="col">Name</th>
						<th scope="col">Quantity</th>
						<th scope="col">Price</th>
						<th scope="col">Description</th>
						<th scope="col">Date</th>
						<th scope="col">Action</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach items="${products}" var="product">

						<tr>
							<td><a
								href="controller?command=product&action=product&productId=${product.id}">${product.title}&ensp;${product.brand}</a></td>
							<td>${product.quantity}</td>
							<td>${product.price}</td>
							<td>${product.description}</td>
							<td>${product.date}</td>
							<td>
								<form name="productDelete" method="POST" action="controller">
									<input type="hidden" name="productId" value="${product.id}" />
									<input type="submit" class="btn btn-danger"
										name="productDelete" value="Delete"> <input
										type="hidden" name="command" value="product" /> <input
										type="hidden" name="action" value="productDelete" />
								</form>
								<form name="productUpdate" method="POST" action="controller">
									<input type="hidden" name="type" value="update" /> <input
										type="hidden" name="productId" value="${product.id}" /> <input
										type="submit" class="btn btn-danger" name="productUpdate"
										value="Update"> <input type="hidden" name="command"
										value="product" /> <input type="hidden" name="action"
										value="productDirect" />
								</form>
							</td>
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