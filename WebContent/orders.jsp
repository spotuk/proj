<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="orders" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>



<body>
	<%-- HEADER --%>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<%-- HEADER --%>
	<c:choose>
		<c:when test="${userRole.name == 'client'}">
			<table class="table table-dark">
				<thead>
					<tr>
						<th scope="col">Name</th>
						<th scope="col">Price</th>
						<th scope="col">Status</th>
						<th scope="col">Date</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach items="${orders}" var="order">

						<tr>
							<td><form name="modelDetails" method="POST"
									action="controller">
									<input type="hidden" name="orderId" value="${order.id}" /> <input
										type="hidden" name="modelNo" value="${product.title}" /> <input
										type="hidden" name="priceTotal" value="${order.price}" /> <input
										type="submit" class="btn btn-danger" name="addToCart"
										value="Order E-${order.id}"> <input type="hidden"
										name="command" value="order" /> <input type="hidden"
										name="action" value="orderDetails" />
								</form></td>
							<td>${order.price}</td>
							<td>${order.status}</td>
							<td>${order.date}</td>
						</tr>

					</c:forEach>
				</tbody>
			</table>
		</c:when>
	</c:choose>

	<c:choose>
		<c:when test="${userRole.name == 'admin'}">
			<table class="table table-dark">
				<thead>
					<tr>
						<th scope="col">Name</th>
						<th scope="col">Login</th>
						<th scope="col">Price</th>
						<th scope="col">Status</th>
						<th scope="col">Date</th>
						<th scope="col">Action</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach items="${orders}" var="order">

						<tr>
							<td><form name="modelDetails" method="POST"
									action="controller">
									<input type="hidden" name="orderId" value="${order.id}" /> <input
										type="hidden" name="modelNo" value="${product.title}" /> <input
										type="hidden" name="priceTotal" value="${order.price}" /> <input
										type="submit" class="btn btn-danger" name="addToCart"
										value="Order E-${order.id}"> <input type="hidden"
										name="command" value="order" /> <input type="hidden"
										name="action" value="orderDetails" />
								</form></td>
							<td>${order.login}</td>
							<td>${order.price}</td>
							<td><form action="">
									<select name="orderStatus" id="inputStatus"
										class="form-control"
										onchange="document.location=this.options[this.selectedIndex].value">



										<option value="#">${order.status}</option>

										<c:choose>
											<c:when test="${order.status == 'canceled'}">
												<option
													value="controller?command=order&action=updateOrderStatus&statusId=1&orderId=${order.id}">registered</option>
												<option
													value="controller?command=order&action=updateOrderStatus&statusId=3&orderId=${order.id}">payd</option>
											</c:when>
											<c:when test="${order.status == 'registered'}">
												<option
													value="controller?command=order&action=updateOrderStatus&statusId=2&orderId=${order.id}">canceled</option>
												<option
													value="controller?command=order&action=updateOrderStatus&statusId=3&orderId=${order.id}">payd</option>
											</c:when>
											<c:when test="${order.status == 'payd'}">
												<option
													value="controller?command=order&action=updateOrderStatus&statusId=1&orderId=${order.id}">registered</option>
												<option
													value="controller?command=order&action=updateOrderStatus&statusId=2&orderId=${order.id}">canceled</option>
											</c:when>
										</c:choose>
									</select>
								</form></td>
							<td>${order.date}</td>
							<td>
								<form name="deleteOrder" method="POST" action="controller">
									<input type="hidden" name="orderId" value="${order.id}" /> <input
										type="submit" class="btn btn-danger" name="deleteOrder"
										value="Delete"> <input type="hidden" name="command"
										value="order" /> <input type="hidden" name="action"
										value="deleteOrder" />
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