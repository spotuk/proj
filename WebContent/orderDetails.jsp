<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:set var="title" value="order details" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>



<body>
	<%-- HEADER --%>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<%-- HEADER --%>

	<table class="table table-dark">
		<thead>
			<tr>
				<th scope="col">Name</th>
				<th scope="col">Price</th>
				<th scope="col">Quantity</th>
				<th scope="col">Sum</th>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${orderDetails}" var="orderDetail">

				<tr>
					<td><a href="controller?command=product&action=product&productId=${orderDetail.productId}">${orderDetail.productTitle}&ensp;${orderDetail.productBrand}</a></td>
					<td>${orderDetail.price}</td>
					<td>${orderDetail.quantity}</td>
					<td>${orderDetail.total}</td>
				</tr>

			</c:forEach>
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td>${priceTotal}</td>
			</tr>
		</tbody>
	</table>



	<!--Section: Products v.3-->

	<!--Pagination-->

	<!--Pagination-->



</body>
</html>